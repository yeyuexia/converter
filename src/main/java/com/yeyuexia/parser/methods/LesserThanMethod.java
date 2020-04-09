package com.yeyuexia.parser.methods;

import com.yeyuexia.parser.Method;
import java.util.List;
import java.util.function.Function;

public class LesserThanMethod extends Method {

  public LesserThanMethod() {
    super("<");
  }

  @Override
  public Function apply(String rType, List<Function> args) {
    Function a = args.get(0);
    Function b = args.get(1);
    return source -> {
      Object applyA = a.apply(source);
      Object applyB = b.apply(source);
      if (!(applyA instanceof Comparable && applyB instanceof Comparable)) {
        throw new RuntimeException();
      }
      return ((Comparable) applyA).compareTo(applyB) < 0;
    };
  }
}
