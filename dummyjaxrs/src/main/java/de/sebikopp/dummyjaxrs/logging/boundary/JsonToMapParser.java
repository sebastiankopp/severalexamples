package de.sebikopp.dummyjaxrs.logging.boundary;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

public class JsonToMapParser {
	
	public static Map<String,Object> fromJsonString(String json) {
		JsonReader jsonReader = Json.createReader(new StringReader(json));
		JsonStructure rootStructure = jsonReader.read();
		if (rootStructure.getValueType() != ValueType.OBJECT) {
			throw new IllegalArgumentException("The contained String does not represent a map/json object.");
		}
		JsonObject rootMap = (JsonObject) rootStructure; 
		return unpackJsonObject(rootMap);
	}
	
	public static Object unpackJsonValue(JsonValue val) {
		switch (val.getValueType()) {
		case ARRAY:
			return unpackJsonArray((JsonArray) val);
		case OBJECT:
			return unpackJsonObject((JsonObject) val);
		case FALSE:
			return Boolean.FALSE;
		case TRUE:
			return Boolean.TRUE;
		case STRING:
			return ((JsonString)val).getString();
		case NULL:
			return null;
		case NUMBER:
			return unpackNumber((JsonNumber) val);
		default:
			throw new RuntimeException("Unknown value type");
		}
	}
	
	private static Map<String,Object> unpackJsonObject(JsonObject obj) {
		final HashMap<String, Object> rc = new HashMap<>();
		obj.forEach((k,v) -> rc.put(k, unpackJsonValue(v)));
		return rc;	
	}
	
	private static List<Object> unpackJsonArray(JsonArray arr) {
		return arr.stream()
				.map(JsonToMapParser::unpackJsonValue)
				.collect(Collectors.toList());
	}
	
	private static Number unpackNumber(JsonNumber nmb) {
		try {
			return nmb.intValueExact();
		} catch (ArithmeticException e0) {
			try {
				return nmb.longValueExact();
			} catch (ArithmeticException e1) {
				try {
					return nmb.bigIntegerValueExact();
				} catch (ArithmeticException e3) {
					return nmb.bigDecimalValue();
				}
			}
		}
	}
}
