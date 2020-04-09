package com.yeyuexia.parser.methods;

import static java.util.stream.Collectors.toList;

import com.yeyuexia.parser.Method;
import java.util.List;
import java.util.function.Function;

public class FilterMethod extends Method {

  public FilterMethod() {
    super("filter");
  }

  @Override
  public Function apply(String rType, List<Function> args) {
    Function list = args.get(0);
    Function method = args.get(1);
    return source -> ((List) list.apply(source)).stream().filter(item -> (boolean)method.apply(item)).map(args.get(2)).collect(toList());
  }
}
