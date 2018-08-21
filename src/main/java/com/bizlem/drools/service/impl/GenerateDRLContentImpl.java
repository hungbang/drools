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
    private static final String MAP_GET = "$map.get(\"";
    private final String newLine = System.lineSeparator();

    @Override
    public String initDrlContent(List<Rules> ruleList, Map<String, String> variableToDataType) {

        StringBuilder buffer = new StringBuilder();
        // Add package structure
        buffer.append(packageAndImportClasses());

        for (Rules rules : ruleList) {
            buffer.append(newLine);
            buffer.append("rule ").append(rules.getRuleName()).append(newLine);
            buffer.append("dialect \"mvel\"");
            buffer.append(newLine).append("when").append(newLine);
            buffer.append("$map: Map (");

            // add condition for each rule
            buffer.append(condition(rules.getInputFieldPair(), variableToDataType));
            buffer.append(")").append(newLine);

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
        StringBuilder buffer = new StringBuilder();

        switch (dataType.trim()) {
            case "String":
                buffer.append(MAP_GET).append(variableName).append("\")").append(" ");
                buffer.append(" in (");
                buffer.append(splitValue(variableValue)).append(") ");
                break;

            case "Integer":
                buffer.append(MAP_GET).append(variableName).append("\")").append(" ");
                buffer.append(checkOperatorInteger(variableValue)).append(" ");
                break;

            default:
                buffer.append(MAP_GET).append(variableName).append("\")").append(" == ");
                buffer.append(variableValue);
        }
        return buffer.toString();
    }

    private String splitValue(String variableValue) {
        return Arrays.stream(variableValue.split(",")).map(s -> "\"" + s + "\"").collect(Collectors.joining(","));
    }

    private String action(Map<String, String> ruleActionFields) {
        StringBuilder buffer = new StringBuilder();
        for (Map.Entry<String, String> entry : ruleActionFields.entrySet()) {

            String variableName = entry.getKey();
            String variableValue = entry.getValue();

            buffer.append("$map.put(\"").append(variableName).append("\"").append(",\"").append(variableValue).append("\"").append(");");
            buffer.append(newLine);
        }
        return buffer.toString();
    }

    private String checkOperatorInteger(String variable) {
        if (variable.startsWith("<") || variable.startsWith(">") || variable.startsWith("==") || variable.startsWith(">=") || variable.startsWith("<="))
            return variable;

        return " == " + variable;
    }

}
