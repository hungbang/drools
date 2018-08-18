package com.bizlem.drools.service.impl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bizlem.drools.model.ExtractedData;
import com.bizlem.drools.model.Rules;
import com.bizlem.drools.service.ExtractDataFromJson;
import com.bizlem.drools.service.GenerateDRLContent;
import com.bizlem.drools.service.GeneratePojoContent;
import com.bizlem.drools.util.ReadFile;
import com.bizlem.drools.util.WriteContentToFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

@Service
@Slf4j
public class ExtractDataFromJsonImpl implements ExtractDataFromJson {

    @Autowired
    GenerateDRLContent drlContent;

    @Autowired
    GeneratePojoContent pojoContent;

//  @Value("${drools.drlFilePath}")
//  private String drlPath;

    @Value("${drools.PojoFilepath}")
    private String pojoPath;


    @Override
    public void extractRulesAndVariableInfo(String inputJson) throws FileNotFoundException {
        final String drlPath = ResourceUtils.getFile("classpath:rules").getPath() + "/";
        log.info("Path of the folder that contains rules file: {}", drlPath);
        ExtractedData extractedData = null;
        Map<String, String> existingVariableNameToDatatype = null;

        // find POJO exist
        if (ReadFile.isFileAvailable(pojoPath))
            existingVariableNameToDatatype = ReadFile.getExistingVariableNameAndDataType();
        else
            existingVariableNameToDatatype = new HashMap<>();

        try {
            extractedData = extractDataFromJson(inputJson);
        } catch (ParseException e) {
            log.error("Error while parsing the json");
            e.printStackTrace();
        }
        // content
        String drlName = extractedData.getDrlName().concat(".drl");
        final String POJO_NAME = "VariablePOJO.java";

        // write DRL File
        String resultContent = drlContent.initDrlContent(extractedData.getRules(), extractedData.getVariableNameToDataType());
        WriteContentToFile.write(drlPath, drlName, resultContent);


        // write POJO File
        Map<String, String> mergedVariableToDataType = mergeVariables(existingVariableNameToDatatype, extractedData.getVariableNameToDataType());

        String resultPojoContent = pojoContent.initPojoContent(mergedVariableToDataType);
        WriteContentToFile.write(pojoPath, POJO_NAME, resultPojoContent);

    }

    public ExtractedData extractDataFromJson(String inputJson) throws ParseException {
        ExtractedData extractedData = new ExtractedData();

        List<Rules> rulesList = new ArrayList<>();

        Map<String, String> variableNameToDataType = new HashMap<>();

        JSONParser parser = new JSONParser();
        JSONObject root = (JSONObject) parser.parse(inputJson);

        String projectName = ((String) root.get("project_name"));
        String loginUserEmail = (String) root.get("current_sfdc_login_user_email");
        String ruleEngineName = (String) root.get("rule_engine_name");

        extractedData.setDrlName(loginUserEmail + "_" + projectName + "_" + ruleEngineName);

        // Get rules array from root object
        JSONArray rulesArray = (JSONArray) root.get("rules");

        // Iterate all json array of rules
        for (Object rules : rulesArray) {
            Rules rule = new Rules();

            JSONObject rulesObj = (JSONObject) rules;

            rule.setRuleName(rulesObj.get("rule_name").toString());

            // get input field value and dataType
            JSONArray inputFields = (JSONArray) rulesObj.get("input_fields");
            rule.setInputFieldPair(gerVariableNameToVariableValuePair(inputFields, variableNameToDataType));

            // get output field value and dataType
            JSONArray outputFields = (JSONArray) rulesObj.get("output_fields");
            rule.setOutputFiledPair(gerVariableNameToVariableValuePair(outputFields, variableNameToDataType));

            // get variableName and data type map
            rulesList.add(rule);
        }
        extractedData.setVariableNameToDataType(variableNameToDataType);
        extractedData.setRules(rulesList);

        return extractedData;
    }

    public Map<String, String> gerVariableNameToVariableValuePair(JSONArray inputFieldsArray, Map<String, String> variableNameToDataType) {
        Map<String, String> variableNameToVariableValue = new HashMap<>();

        // Take all data of input fields in type and value
        for (Object obj : inputFieldsArray) {
            JSONObject singleField = (JSONObject) obj;
            Set<String> keySet = singleField.keySet();
            String variableName = getVariableName(keySet);

            // Feed data to name and datatype pair
            variableNameToDataType.put(variableName, singleField.get("type").toString());

            // feed data to name and value pair
            variableNameToVariableValue.put(variableName, singleField.get(variableName).toString());
        }
        return variableNameToVariableValue;
    }

    private String getVariableName(Set<String> keySet) {
        String variable = (String) keySet.toArray()[0];
        return variable.equals("type") ? (String) keySet.toArray()[1] : variable;
    }

    private Map<String, String> mergeVariables(Map<String, String> existingVariable, Map<String, String> requestedVariable) {
        Map<String, String> allVariable = new HashMap<>();
        allVariable.putAll(existingVariable);
        allVariable.putAll(requestedVariable);
        return allVariable;
    }

    public static void main(String[] args) {
        Map<String, String> map1 = new HashMap<>();
        map1.put("var1", "val1");
        map1.put("var2", "val2");
        map1.put("var3", "val3");
        map1.put("var4", "val4");


        Map<String, String> map2 = new HashMap<>();
        map2.put("var1", "val1");
        map2.put("var4", "val4");

        map1.putAll(map2);

        map1.forEach((k, v) -> System.out.println("Key : " + k + " Value :" + v));
    }

}