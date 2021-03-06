package com.bizlem.drools.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtractedData {

  private List<Rules> rules;
  private Multimap<String, String> variableNameToDataType;
  private String drlName;
}
