package com.bizlem.drools.service.impl;

import java.io.File;
import java.io.FileNotFoundException;

import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bizlem.drools.model.VariablePOJO;
import com.bizlem.drools.service.RuleCallingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

@Service
@Slf4j
public class RuleCallingServiceImpl implements RuleCallingService {


  @Override
  public VariablePOJO callRules(String drlName, VariablePOJO variablePOJO) throws FileNotFoundException {
    final String drlPath = ResourceUtils.getFile("classpath:rules").getPath() + "/";
    log.info("Path of the folder that contains rules file: {}", drlPath);
    VariablePOJO ruleInput = variablePOJO;

    KieServices kieServices = KieServices.Factory.get();
    File file = new File(drlPath.concat(drlName));
    Resource resource = kieServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DRL);
    KieFileSystem kFileSystem = kieServices.newKieFileSystem().write(resource);
    kieServices.newKieBuilder(kFileSystem).buildAll();
    
    KieContainer kc = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());

    KieSession kSession = kc.newKieSession();
    kSession.insert(ruleInput);
    kSession.fireAllRules();
    
    log.info("fired all rules");
    return ruleInput;
  }

}