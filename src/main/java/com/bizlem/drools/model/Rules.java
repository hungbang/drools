package com.bizlem.drools.model;

import java.util.Map;

import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Rules {
  private String ruleName;
  private Multimap<String, String> inputFieldPair;
  private Multimap<String, String> outputFiledPair;
}
