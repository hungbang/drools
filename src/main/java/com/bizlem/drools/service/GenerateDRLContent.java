package com.bizlem.drools.service;

import java.util.List;
import java.util.Map;
import com.bizlem.drools.model.Rules;
import com.google.common.collect.Multimap;

public interface GenerateDRLContent {

  String initDrlContent(List<Rules> ruleList, Multimap<String, String> variableNameToDataType);

}
