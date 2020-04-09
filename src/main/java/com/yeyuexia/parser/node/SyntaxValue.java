package com.yeyuexia.parser.node;

import org.apache.commons.lang3.math.NumberUtils;

public class SyntaxValue implements SyntaxNode {

  private String value;
  private boolean constant;

  public SyntaxValue(String value) {
    this.value = value.startsWith("\"") ? value.substring(1, value.length() - 1) : value;
    constant = value.startsWith("\"") || NumberUtils.isCreatable(value);
  }

  public String getValue() {
    return value;
  }

  public boolean isConstant() {
    return constant;
  }
}
