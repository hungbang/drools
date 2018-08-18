package com.bizlem.drools.model;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtractedData {

  private List<Rules> rules;
  private Map<String, String> variableNameToDataType;
  private String drlName;
}
