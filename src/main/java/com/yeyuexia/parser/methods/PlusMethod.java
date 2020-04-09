package com.yeyuexia.parser.methods;

import com.yeyuexia.parser.Method;
import java.util.List;
import java.util.function.Function;

public class PlusMethod extends Method {

  public PlusMethod() {
    super("+");
  }

  @Override
  public Function apply(String rType, List<Function> args) {
    switch (rType) {
      case "int":
        return source -> args.stream().mapToInt(arg -> (int) (arg.apply(source))).reduce(0, (a, b) -> a + b);
      case "string":
        return source -> args.stream().map(arg -> (String) (arg.apply(source))).reduce("", (a, b) -> a + b);
      default:
        throw new RuntimeException();
    }
  }
}
