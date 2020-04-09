package com.yeyuexia.parser;

import com.yeyuexia.parser.methods.AssignMethod;
import com.yeyuexia.parser.methods.ConditionMethod;
import com.yeyuexia.parser.methods.EqualMethod;
import com.yeyuexia.parser.methods.FilterMethod;
import com.yeyuexia.parser.methods.ForEachMethod;
import com.yeyuexia.parser.methods.LesserThanMethod;
import com.yeyuexia.parser.methods.MinusMethod;
import com.yeyuexia.parser.methods.PlusMethod;
import com.yeyuexia.parser.node.SyntaxLeaf;
import com.yeyuexia.parser.node.SyntaxNode;
import com.yeyuexia.parser.node.SyntaxValue;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Parser {

  private Map<String, ConvertFunction> methods = new HashMap<>();
  private SyntaxTreeBuilder builder = new SyntaxTreeBuilder();

  public Parser() {
    Stream.of(new AssignMethod(), new ConditionMethod(), new LesserThanMethod(), new MinusMethod(), new PlusMethod(),
        new EqualMethod(), new FilterMethod(), new ForEachMethod())
        .forEach(method -> methods.put(method.getKeyword(), method));
  }

  protected abstract Object unwrapValue(String source, String expression);

  public Function<String, ?> read(String type, String content) {
    SyntaxNode root = builder.build(content);
    return parseTree(type, root);
  }

  private Function parseTree(String type, SyntaxNode node) {
    if (node instanceof SyntaxValue) {
      return wrapValue((SyntaxValue) node);
    }
    if (node instanceof SyntaxLeaf) {
      SyntaxLeaf leaf = (SyntaxLeaf) node;
      return methods.get(leaf.getMethodName()).apply(type, leaf.getArgs()
          .stream()
          .map(arg -> {
            if (arg instanceof SyntaxValue) {
              return wrapValue((SyntaxValue) arg);
            } else {
              return parseTree(type, arg);
            }
          }).collect(Collectors.toList()));
    } else {
      throw new RuntimeException();
    }
  }

  private Function<Object, Object> wrapValue(SyntaxValue value) {
    return source -> value.isConstant() ? value.getValue() : unwrap(source, value.getValue());
  }

  private Object unwrap(Object source, String expression) {
    if (source instanceof String) {
      return unwrapValue((String) source, expression);
    } else if (source instanceof Map) {
      Map result = (Map) source;
      String[] split = expression.split("\\.");
      for (int i = 0; i < split.length - 1; i++) {
        result = (Map) result.get(split[i]);
      }
      return result.get(split[split.length-1]);
    }
    return null;
  }
}
