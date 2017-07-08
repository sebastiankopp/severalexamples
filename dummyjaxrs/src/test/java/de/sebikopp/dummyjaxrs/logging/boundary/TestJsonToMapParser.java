package de.sebikopp.dummyjaxrs.logging.boundary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class TestJsonToMapParser {
	@Test(expected=IllegalArgumentException.class)
	public void test1() {
		String json = "[\"foo\", \"bar\"], \"baz\"";
		JsonToMapParser.fromJsonString(json);
	}
	
	@Test
	public void test2() {
		String json = "{\"content\":[\"foo\", \"bar\", \"baz\"]}";
		Map<String, Object> jsonMap = JsonToMapParser.fromJsonString(json);
		Assert.assertEquals(1, jsonMap.size());
		List<String> lst = (List<String>) jsonMap.get("content");
		String[] expectedVals = {"foo", "bar", "baz"};
		Assert.assertArrayEquals(expectedVals, lst.stream().toArray(String[]::new));
		
	}
	
	@Test()
	public void test3() {
		String json = "{\"foo\":[47114711122, 815], \"bar\":[333e-3]}";
		Map<String, Object> jsonMap = JsonToMapParser.fromJsonString(json);
		List<Object> barVal = (List<Object>) jsonMap.get("bar");
		Assert.assertEquals(BigDecimal.class, (barVal.get(0).getClass()));
		List<Object> fooVal = (List<Object>) jsonMap.get("foo");
		Class<?>[] actuals = fooVal.stream().map(Object::getClass).toArray(Class[]::new);
		Class<?>[] expectedClasses = {Long.class, Integer.class};
		Assert.assertArrayEquals(expectedClasses, actuals);
	}

}
