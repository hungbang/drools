package com.bizlem.drools.service;

import com.bizlem.drools.model.VariablePOJO;

import java.io.FileNotFoundException;
import java.util.Map;

public interface RuleCallingService {

  Map<String, String> callRules(String drlName, Map<String, String> variablePOJO) throws FileNotFoundException;

}
