package com.unravel.scout.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
  private static ObjectMapper mapper = new ObjectMapper();

  public static <T> String getString(T object) throws JsonProcessingException {
    return mapper.writeValueAsString(object);
  }

  public static <T> T getObject(String string, Class<T> className) throws JsonProcessingException {
    return mapper.readValue(string, className);
  }
}
