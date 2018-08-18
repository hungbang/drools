package com.bizlem.drools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bizlem.drools.model.VariablePOJO;
import com.bizlem.drools.service.ExtractDataFromJson;
import com.bizlem.drools.service.RuleCallingService;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/drools")
@Slf4j
public class RuleProcessingController {

  @Autowired
  ExtractDataFromJson drlGeneratorService;
  

  @Autowired
  RuleCallingService ruleCallingService;

  @PostMapping("/generaterules")
  public ResponseEntity<String> processRuleJson(@RequestBody String inputJson) throws FileNotFoundException {
    drlGeneratorService.extractRulesAndVariableInfo(inputJson);
    log.info("method calling processRuleJson");

    return ResponseEntity.ok().build();
  }

  @PostMapping("/callrules/{ruleFileName}/fire")
  public VariablePOJO callRules(@PathVariable(value = "ruleFileName") String ruleFileName, @RequestBody VariablePOJO pojo) throws FileNotFoundException {
    String drl = ruleFileName.concat(".drl");
    log.info("input value for calling pojo :{}", drl);
    VariablePOJO updatedPOJO = ruleCallingService.callRules(drl, pojo);

    return updatedPOJO;
  }
}
