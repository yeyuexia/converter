package com.yeyuexia.parser;

import com.jayway.jsonpath.JsonPath;

public class JsonPathParser extends Parser {

  @Override
  protected Object unwrapValue(String source, String expression) {
    return JsonPath.read(source, expression);
  }
}
