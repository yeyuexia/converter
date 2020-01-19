package com.yeyuexia.parser;

import java.util.List;
import java.util.function.Function;

public interface ConvertFunction {
  Function apply(String rType, List<Function> args);
}
