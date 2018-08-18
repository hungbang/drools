package com.bizlem.drools.service;

import com.bizlem.drools.model.VariablePOJO;

import java.io.FileNotFoundException;

public interface RuleCallingService {

  VariablePOJO callRules(String drlName, VariablePOJO variablePOJO) throws FileNotFoundException;

}
