package com.yeyuexia.parser.methods;

import com.yeyuexia.parser.Method;
import java.util.List;
import java.util.function.Function;

public class MinusMethod extends Method {

  public MinusMethod() {
    super("-");
  }

  @Override
  public Function apply(String rType, List<Function> args) {
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
  }
}
