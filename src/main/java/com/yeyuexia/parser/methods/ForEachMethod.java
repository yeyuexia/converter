package com.yeyuexia.parser.methods;

import static java.util.stream.Collectors.toList;

import com.yeyuexia.parser.Method;
import java.util.List;
import java.util.function.Function;

public class ForEachMethod extends Method {

  public ForEachMethod() {
    super("loop");
  }

  @Override
  public Function apply(String rType, List<Function> args) {
    return source -> ((List)args.get(0).apply(source)).stream().map(args.get(1)).collect(toList());
  }
}
