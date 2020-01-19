package com.yeyuexia.parser;

public class SyntaxValue implements SyntaxNode {

  private String value;

  public SyntaxValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
