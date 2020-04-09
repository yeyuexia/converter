package com.yeyuexia.parser.methods;

import com.yeyuexia.parser.Method;
import java.util.List;
import java.util.function.Function;

public class EqualMethod extends Method {

  public EqualMethod() {
    super("==");
  }

  @Override
  public Function apply(String rType, List<Function> args) {
    return source -> args.get(0).apply(source).equals(args.get(1).apply(source));
  }
}
