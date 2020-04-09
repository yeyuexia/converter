package com.yeyuexia.parser;

import com.yeyuexia.parser.node.SyntaxLeaf;
import com.yeyuexia.parser.node.SyntaxNode;
import com.yeyuexia.parser.node.SyntaxValue;

public class SyntaxTreeBuilder {

  private int index;

  public SyntaxNode build(String content) {
    index = 0;
    if (content.startsWith("(")) {
      return buildNode(content);
    }
    return new SyntaxValue(content);
  }

  private SyntaxLeaf buildNode(String content) {
    if (content.startsWith("(")) {
      index += 1;
    }
    int endIndex = content.substring(index).indexOf(' ') + index;
    String methodName = content.substring(index, endIndex);
    SyntaxLeaf node = new SyntaxLeaf(methodName);
    index = endIndex + 1;

    while (index < content.length() && content.charAt(index) != ')') {
      switch (content.charAt(index)) {
        case '(':
          node.getArgs().add(buildNode(content));
          break;
        case ' ':
          index += 1;
          break;
        case '\"':
          endIndex = content.indexOf('\"', index + 1);
          node.getArgs().add(new SyntaxValue(content.substring(index, endIndex + 1)));
          index = endIndex + 1;
          break;
        default:
          int i = content.indexOf(' ', index);
          int i2 = content.indexOf(')', index);
          if (i == -1 || i > i2) {
            endIndex = i2;
            node.getArgs().add(new SyntaxValue(content.substring(index, endIndex)));
            index = endIndex;
          } else {
            endIndex = i;
            node.getArgs().add(new SyntaxValue(content.substring(index, endIndex)));
            index = endIndex + 1;
          }
      }
    }
    index += 1;
    return node;
  }
}
