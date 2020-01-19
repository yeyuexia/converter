package com.yeyuexia.parser;

import com.jayway.jsonpath.JsonPath;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Parser {

  private Map<String, ConvertFunction> methods = new HashMap<>();

  private int index = 0;

  public Parser() {
    methods.put("+", (rType, args) -> {
      switch (rType) {
        case "int":
          return source -> args.stream().mapToInt(arg -> (int) (arg.apply(source))).reduce(0, (a, b) -> a + b);
        case "string":
          return source -> args.stream().map(arg -> (String) (arg.apply(source))).reduce("", (a, b) -> a + b);
        default:
          throw new RuntimeException();
      }
    });
    methods.put("when", (rType, args) -> {
      Function condition = args.get(0);
      Function then = args.get(1);
      Function otherwise = args.get(2);
      return source -> {
        if ((boolean) condition.apply(source)) {
          return then.apply(source);
        } else {
          return otherwise.apply(source);
        }
      };
    });
    methods.put("-", (rType, args) -> {
      switch (rType) {
        case "int":
          return source -> args.stream().mapToInt(arg -> (int) (arg.apply(source))).reduce(0, (a, b) -> a - b);
        case "number":
          return source -> args.stream()
              .mapToDouble(arg -> (double) (arg.apply(source))).reduce((a, b) -> a - b)
              .orElseThrow(() -> new RuntimeException());
        default:
          throw new RuntimeException();
      }
    });
    methods.put(">", (rType, args) -> lessThan(args.get(0), args.get(1)));
    methods.put("=", (rType, args) -> args.get(0));
  }

  public Function<String, Boolean> lessThan(Function a, Function b) {
    return source -> {
      Object applyA = a.apply(source);
      Object applyB = b.apply(source);
      if (!(applyA instanceof Comparable && applyB instanceof Comparable)) {
        throw new RuntimeException();
      }
      return ((Comparable)applyA).compareTo(applyB) < 0;
    };
  }

  public Function<String, ?> read(String type, String content) {
    SyntaxNode root;
    if (content.charAt(index) == '(') {
      index += 1;
      root = buildNode(content);
      return parserFunction(type, root);
    } else {
      return wrapperValue(new SyntaxValue(content));
    }
  }

  private SyntaxLeaf buildNode(String content) {
    int endIndex = content.substring(index).indexOf(' ') + index;
    String methodName = content.substring(index, endIndex);
    SyntaxLeaf node = new SyntaxLeaf(methodName);
    index = endIndex + 1;

    while (content.charAt(index) != ')') {
      switch (content.charAt(index)) {
        case '(':
          index += 1;
          node.getArgs().add(buildNode(content));
          break;
        case ' ':
          index += 1;
          break;
        default:
          int i = content.substring(index).indexOf(' ');
          int i2 = content.substring(index).indexOf(')');
          if (i == -1 || i > i2) {
            endIndex = i2 + index;
            node.getArgs().add(new SyntaxValue(content.substring(index, endIndex)));
            index = endIndex;
          } else {
            endIndex = i + index;
            node.getArgs().add(new SyntaxValue(content.substring(index, endIndex)));
            index = endIndex + 1;
          }
      }
    }
    index += 1;
    return node;
  }

  private Function parserFunction(String type, SyntaxNode node) {
    if (node instanceof SyntaxValue) {
      return wrapperValue((SyntaxValue) node);
    }
    if (node instanceof SyntaxLeaf) {
      SyntaxLeaf leaf = (SyntaxLeaf) node;
      return methods.get(leaf.getMethodName()).apply(type, leaf.getArgs()
          .stream()
          .map(arg -> {
            if (arg instanceof SyntaxValue) {
              return wrapperValue((SyntaxValue) arg);
            } else if (arg instanceof SyntaxLeaf) {
              return parserFunction(type, arg);
            } else {
              throw new RuntimeException();
            }
          }).collect(Collectors.toList()));
    } else {
      throw new RuntimeException();
    }
  }

  private Function<String, Object> wrapperValue(SyntaxValue value) {
    return source -> JsonPath.read(source, value.getValue());
  }

}
