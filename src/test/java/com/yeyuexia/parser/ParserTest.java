package com.yeyuexia.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.function.Function;
import org.junit.Test;

public class ParserTest {
  private static final String JSON = "{\n"
      + "  \"routeCode\":\"TM618\",\n"
      + "  \"productId\":\"GAP527813_1_000466996_H105-11304_M03-0003\",\n"
      + "  \"productCode\":\"spu_0001\",\n"
      + "  \"schemaCode\":\"s101\",\n"
      + "  \"tenantCode\":\"8800001\",\n"
      + "  \"master\":{\n"
      + "    \"style\":\"spu_0001\",\n"
      + "    \"title\":\"T恤（促销款）\",\n"
      + "    \"sizeChart\":[\n"
      + "      {\n"
      + "        \"high\":\"150\",\n"
      + "        \"weight\":\"120\",\n"
      + "        \"value\":\"S\"\n"
      + "      },\n"
      + "      {\n"
      + "        \"high\":\"150\",\n"
      + "        \"weight\":\"125\",\n"
      + "        \"value\":\"M\"\n"
      + "      },\n"
      + "      {\n"
      + "        \"high\":\"150\",\n"
      + "        \"weight\":\"130\",\n"
      + "        \"value\":\"L\"\n"
      + "      }\n"
      + "    ],\n"
      + "    \"marketTime\":\"2019-10-02 12:23:23\",\n"
      + "    \"description\":\"打折款，超级便宜的衣服\",\n"
      + "    \"freeShip\":\"上海市免运费\",\n"
      + "    \"component\":[\n"
      + "      \"羊毛\",\n"
      + "      \"鸭绒\"\n"
      + "    ],\n"
      + "    \"image\":\"http:xxxxx/image.jpg\",\n"
      + "    \"video\":\"http:xxxx/video.mp4\"\n"
      + "  },\n"
      + "  \"variants\":[\n"
      + "    {\n"
      + "      \"variantId\":\"0001\",\n"
      + "      \"variantCode\":\"sku_001\",\n"
      + "      \"detail\":{\n"
      + "        \"size\":\"L\",\n"
      + "        \"color\":\"red\"\n"
      + "      }\n"
      + "    },\n"
      + "    {\n"
      + "      \"variantId\":\"0002\",\n"
      + "      \"variantCode\":\"sku_002\",\n"
      + "      \"detail\":{\n"
      + "        \"size\":\"L\",\n"
      + "        \"color\":\"blue\"\n"
      + "      }\n"
      + "    }\n"
      + "  ]\n"
      + "}";

  @Test
  public void should_success_parse_logical() {
    Parser parser = new JsonPathParser();
    Function<String, ?> function = parser.read("string", "(+ (+ schemaCode master.title master.style \"_ 111\") routeCode)");
    Object result = function.apply(JSON);

    assertEquals("s101T恤（促销款）spu_0001_ 111TM618", result);
  }

  @Test
  public void should_success_support_when_operation() {
    Parser parser = new JsonPathParser();
    Function<String, ?> function = parser.read("string", "(if (< productId tenantCode) routeCode productId))");
    Object result = function.apply(JSON);

    assertEquals("GAP527813_1_000466996_H105-11304_M03-0003", result);
  }

  @Test
  public void should_success_support_equal_operation() {
    Parser parser = new JsonPathParser();
    Function<String, ?> function = parser.read("string", "(= master))");
    Object result = function.apply(JSON);

    assertNotNull(result);
  }

  @Test
  public void should_success_support_constant_string() {
    Parser parser = new JsonPathParser();
    String content = "\"string value\"";

    Function<String, ?> function = parser.read("string", content);
    Object result = function.apply(JSON);

    assertEquals(content.substring(1, content.length() - 1), result);
  }

  @Test
  public void should_support_simple_loop() {
    Parser parser = new JsonPathParser();

    Function<String, ?> function = parser.read("string", "(loop master.sizeChart (= high))");
    Object result = function.apply(JSON);

    assertEquals("[150, 150, 150]", result.toString());
  }

  @Test
  public void should_support_loop() {
    Parser parser = new JsonPathParser();

    Function<String, ?> function = parser.read("string", "(loop variants (= detail.size))");
    Object result = function.apply(JSON);

    assertEquals("[L, L]", result.toString());
  }

  @Test
  public void should_support_condition_loop() {
    Parser parser = new JsonPathParser();

    Function<String, ?> function = parser.read("string", "(filter master.sizeChart (== weight 120) (= high))");
    Object result = function.apply(JSON);

    assertEquals("[150]", result.toString());
  }
}