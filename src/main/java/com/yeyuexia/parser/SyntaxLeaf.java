package com.yeyuexia.parser;

import com.google.common.collect.Lists;
import java.util.List;

public class SyntaxLeaf implements SyntaxNode {
  private String methodName;
  private List<SyntaxNode> args;

  public SyntaxLeaf(String methodName) {
    this.methodName = methodName;
    this.args = Lists.newArrayList();
  }

  public String getMethodName() {
    return methodName;
  }

  public List<SyntaxNode> getArgs() {
    return args;
  }
}
