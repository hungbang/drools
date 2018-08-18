package com.bizlem.drools.model;

import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Rules {
  private String ruleName;
  private Map<String, String> inputFieldPair;
  private Map<String, String> outputFiledPair;
}
