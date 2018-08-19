package com.bizlem.drools.controller;

import com.bizlem.drools.service.ExtractDataFromJson;
import com.bizlem.drools.service.RuleCallingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.Map;

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
  public Map<String, String> callRules(@PathVariable(value = "ruleFileName") String ruleFileName, @RequestBody Map<String, String> pojo) throws FileNotFoundException {
    String drl = ruleFileName.concat(".drl");
    log.info("input value for calling pojo :{}", drl);
    Map<String, String> updatedPOJO = ruleCallingService.callRules(drl, pojo);

    return updatedPOJO;
  }
}
