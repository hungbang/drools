package com.bizlem.drools.service.impl;

import com.bizlem.drools.service.RuleCallingService;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class RuleCallingServiceImpl implements RuleCallingService {
  final String drlPath = System.getProperty("com.bizlem.appDir") + File.separator;
  @Override
  public Map<String, String> callRules(String drlName, Map<String, String> variablePOJO) throws IOException {
    log.info("Path of the folder that contains rules file: {}", drlPath);
    KieServices kieServices = KieServices.Factory.get();
    File file = new File(drlPath.concat(drlName));
    Resource resource = kieServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DRL);
    KieFileSystem kFileSystem = kieServices.newKieFileSystem().write(resource);
    kieServices.newKieBuilder(kFileSystem).buildAll();
    
    KieContainer kc = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());

    KieSession kSession = kc.newKieSession();
    kSession.insert(variablePOJO);
    kSession.fireAllRules();
    
    log.info("fired all rules");
    return variablePOJO;
  }

}
