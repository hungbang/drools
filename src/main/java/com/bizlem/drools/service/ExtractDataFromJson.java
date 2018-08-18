package com.bizlem.drools.service;

import java.io.FileNotFoundException;

public interface ExtractDataFromJson {
  void extractRulesAndVariableInfo(String inputJson) throws FileNotFoundException;
}
