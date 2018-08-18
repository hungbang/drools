package com.bizlem.drools.service;

import java.util.List;
import java.util.Map;
import com.bizlem.drools.model.Rules;

public interface GenerateDRLContent {

  String initDrlContent(List<Rules> ruleList, Map<String, String> variableNameToDataType);

}
