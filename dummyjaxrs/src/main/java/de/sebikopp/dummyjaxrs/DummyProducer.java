package de.sebikopp.dummyjaxrs;

import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("bla")
public class DummyProducer {

	@GET
	public JsonArray sth() {
		return Stream.generate(UUID::randomUUID)
				.limit(25L)
				.map(Object::toString)
				.collect(Collectors.collectingAndThen(
						Collector.of(Json::createArrayBuilder,
								JsonArrayBuilder::add,
								(JsonArrayBuilder left, JsonArrayBuilder right) ->  {
									left.add(right);
									return left;
								}),
						JsonArrayBuilder::build));
	}
}
