package com.bizlem.drools.util;

import org.drools.core.spi.KnowledgeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by KAI on 8/18/18.
 * Copyright 2018 by drools
 * All rights reserved.
 */
public class Helper {
    public static final Logger LOGGER = LoggerFactory.getLogger(Helper.class);
    public static void log(final KnowledgeHelper drools, final String message){
        System.out.println(message);
        System.out.println("\nrule triggered: " + drools.getRule().getName());
        System.out.println(drools.getFactHandle(drools.getMatch()));
    }
}
