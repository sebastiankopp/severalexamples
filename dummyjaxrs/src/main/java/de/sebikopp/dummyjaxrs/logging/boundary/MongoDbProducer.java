package de.sebikopp.dummyjaxrs.logging.boundary;

import java.util.Optional;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

@Logged
public class MongoDbProducer {
	@Inject
	MongoClient clnt;
	
	final static String DEFAULT_DB_NAME = "logging";
	
	@Produces
	public MongoDatabase getDatabase(InjectionPoint injp) {
		String dbName = Optional.ofNullable(injp.getAnnotated().getAnnotation(MongoDatabaseName.class))
				.map(MongoDatabaseName::value)
				.orElse(DEFAULT_DB_NAME);
		return clnt.getDatabase(dbName);
	}

}
