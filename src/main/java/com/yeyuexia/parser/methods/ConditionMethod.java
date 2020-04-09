package com.yeyuexia.parser.methods;

import com.yeyuexia.parser.Method;
import java.util.List;
import java.util.function.Function;

public class ConditionMethod extends Method {

  public ConditionMethod() {
    super("if");
  }

  @Override
  public Function apply(String rType, List<Function> args) {
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
  }
}
