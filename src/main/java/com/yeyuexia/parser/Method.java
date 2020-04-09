package com.yeyuexia.parser;

public abstract class Method implements ConvertFunction {
  private final String keyword;

  public Method(String keyword) {
    this.keyword = keyword;
  }

  public String getKeyword() {
    return keyword;
  }
}
