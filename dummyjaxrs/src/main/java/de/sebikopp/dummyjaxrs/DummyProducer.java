package de.sebikopp.dummyjaxrs;

import java.util.UUID;
import java.util.stream.Stream;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.logging.log4j.Logger;

import com.mongodb.MongoClient;

import de.sebikopp.dummyjaxrs.boundary.JsonCollectors;
import de.sebikopp.dummyjaxrs.people.control.PeopleStore;

@Path("bla")
public class DummyProducer {

	@GET
	public JsonArray sth() {
		return Stream.generate(UUID::randomUUID)
				.limit(25L)
				.map(Object::toString)
				.collect(JsonCollectors.collectStrings());
	}
	@GET
	@Path("specifi")
	public JsonObject getSystemSpecificCfg() {
		CDI<Object> currentCdi = CDI.current();
		JsonObjectBuilder resBui = Json.createObjectBuilder();
		resBui.add("cdiProvider", currentCdi.getClass().getName());
		Instance<Logger> lggrInstance = currentCdi.select(Logger.class);
		final BeanManager beanManager = currentCdi.getBeanManager();
		Logger logger = lggrInstance.get();
		@SuppressWarnings("unchecked")
		Bean<MongoClient> bean = (Bean<MongoClient>) beanManager.getBeans(MongoClient.class).iterator().next();
		CreationalContext<MongoClient> context = beanManager.createCreationalContext(bean);
		MongoClient mongoClient = bean.create(context);
		logger.info("Instance of injected mongo client {}", mongoClient);
		PeopleStore peopleStore = currentCdi.select(PeopleStore.class).get();
		logger.info("+++++++ PEOPLE +++++++");
		peopleStore.getAll().stream()
				.forEach(x -> logger.info("Person: {}", x.getLastName()));
		return resBui.build();
	}
}
