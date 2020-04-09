package com.yeyuexia.parser.methods;

import com.yeyuexia.parser.Method;
import java.util.List;
import java.util.function.Function;

public class AssignMethod extends Method {

  public AssignMethod() {
    super("=");
  }

  @Override
  public Function apply(String rType, List<Function> args) {
    return args.get(0);
  }
}
