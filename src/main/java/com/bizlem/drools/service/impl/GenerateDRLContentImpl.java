package com.bizlem.drools.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.bizlem.drools.model.Rules;
import com.bizlem.drools.service.GenerateDRLContent;

@Service
public class GenerateDRLContentImpl implements GenerateDRLContent {
    String newLine = System.lineSeparator();

    @Override
    public String initDrlContent(List<Rules> ruleList, Map<String, String> variableToDataType) {

        StringBuffer buffer = new StringBuffer();
        // Add package structure
        buffer.append(packageAndImportClasses());

        for (Rules rules : ruleList) {
            buffer.append(newLine);
            buffer.append("rule " + rules.getRuleName() + newLine);
            buffer.append("dialect \"mvel\"");
            buffer.append(newLine + "when" + newLine);
            buffer.append("$map: Map (");

            // add condition for each rule
            buffer.append(condition(rules.getInputFieldPair(), variableToDataType));
            buffer.append(")" + newLine);

            buffer.append("then");
            buffer.append(newLine);

            // add action for each rule
            buffer.append(action(rules.getOutputFiledPair()));

            // add end of the rule
            buffer.append("end");
            buffer.append(newLine);
        }
        return buffer.toString();
    }

    private String packageAndImportClasses() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("package com.bizlem;");
        buffer.append(newLine);
        buffer.append(newLine);
        buffer.append("import java.util.Map;");
        buffer.append(newLine);
        return buffer.toString();
    }

    private String condition(Map<String, String> ruleConditionFields, Map<String, String> variableNameToDataType) {
        List<String> condition = new ArrayList<String>();
        for (Map.Entry<String, String> entry : ruleConditionFields.entrySet()) {
            String variableName = entry.getKey();
            String variableValue = entry.getValue();
            String dataType = variableNameToDataType.get(entry.getKey());

            condition.add(valueOnDataType(variableName, variableValue, dataType));
        }
        return String.join(" && ", condition);
    }

    private String valueOnDataType(String variableName, String variableValue, String dataType) {
        StringBuffer buffer = new StringBuffer();

        switch (dataType.trim()) {
            case "String":
                buffer.append("$map.get(\"" + variableName + "\")" + " ");
                buffer.append(" in (");
                buffer.append(splitValue(variableValue) + ") ");
                break;

            case "Integer":
                buffer.append("$map.get(\"" + variableName + "\")" + " ");
                buffer.append(checkOperatorInteger(variableValue) + " ");
                break;

            default:
                buffer.append("$map.get(\"" + variableName + "\")" + " == ");
                buffer.append(variableValue);
        }
        return buffer.toString();
    }

    private String splitValue(String variableValue) {
        return Arrays.stream(variableValue.split(",")).map(s -> "\"" + s + "\"").collect(Collectors.joining(","));
    }

    private String action(Map<String, String> ruleActionFields) {
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String> entry : ruleActionFields.entrySet()) {

            String variableName = entry.getKey();
            String variableValue = entry.getValue();

            buffer.append("$map.put(\"" + variableName + "\"" + ",\"" + variableValue + "\"" + ");");
            buffer.append(newLine);
        }
        return buffer.toString();
    }

    private String checkOperatorInteger(String variable) {
        if (variable.startsWith("<") || variable.startsWith(">") || variable.startsWith("=="))
            return variable;

        return " == " + variable;
    }

}
