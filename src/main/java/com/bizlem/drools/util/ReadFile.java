package com.bizlem.drools.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.bizlem.drools.model.VariablePOJO;

public class ReadFile {
  private static final String FILE_NAME = "VariablePOJO.java";

  public static Map<String, String> getExistingVariableNameAndDataType() {

    Field[] fields = VariablePOJO.class.getDeclaredFields();
    Map<String, String> variableNameToDataType = new HashMap<>();
    Arrays.asList(fields).stream().forEach(f -> variableNameToDataType.put(f.getName(), f.getType().getSimpleName()));

    return variableNameToDataType;
  }

  public static boolean isFileAvailable(String filePath) {
    File f = new File(filePath.concat(FILE_NAME));
    return (f.exists() && !f.isDirectory()) ? true : false;
  }
}
