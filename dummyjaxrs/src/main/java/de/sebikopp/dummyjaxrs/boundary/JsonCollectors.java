package de.sebikopp.dummyjaxrs.boundary;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

final public class JsonCollectors {
	
	private JsonCollectors () throws IllegalAccessException {
		throw new IllegalAccessException("Instantiation of this class (also via reflection) is prohibited.");
	}
	
	public static <X extends CharSequence> Collector<X,?,JsonArray> collectStrings() {
		return Collectors.collectingAndThen(Collector.of(
					Json::createArrayBuilder,
					(bui, str) -> bui.add(str.toString()), 
					(left, right) -> { left.add(right); return left; }
				), JsonArrayBuilder::build);
	}

	public static Collector<JsonObject,?,JsonArray> objColl () {
		return Collectors.collectingAndThen(Collector.of(
					Json::createArrayBuilder,
					JsonArrayBuilder::add,
					(left, right) -> { left.add(right); return left; }
				), JsonArrayBuilder::build);
	}
}
