package com.bizlem.drools.controller;

import com.bizlem.drools.service.ExtractDataFromJson;
import com.bizlem.drools.service.RuleCallingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/drools")
@Slf4j
public class RuleProcessingController {

    @Autowired
    private
    ExtractDataFromJson drlGeneratorService;
    @Autowired
    private
    RuleCallingService ruleCallingService;

    @PostMapping("/generaterules")
    public ResponseEntity<String> processRuleJson(@RequestBody String inputJson) throws IOException {
        drlGeneratorService.extractRulesAndVariableInfo(inputJson);
        log.info("method calling processRuleJson");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/callrules/{ruleFileName}/fire")
    public Map<String, String> callRules(@PathVariable(value = "ruleFileName") String ruleFileName, @RequestBody Map<String, String> pojo) throws IOException {
        String drl = ruleFileName.concat(".drl");
        log.info("input value for calling pojo :{}", drl);

        return ruleCallingService.callRules(drl, pojo);
    }
}
