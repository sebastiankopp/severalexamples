package de.sebikopp.dummyjaxrs.logging.boundary;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import com.mongodb.MongoClient;

public class MongoClientProducer {
	
	private final static String PROP_FILE_PTH = "mongoclnt.properties";
	private final static String PROP_HOST = "host";
	private final static String PROP_PORT = "port";
	
	private final Properties mongoprops = new Properties();
	
	@Inject
	Logger logger;
	
	@PostConstruct
	void init() {
		try {
			mongoprops.load(getClass().getResourceAsStream(PROP_FILE_PTH));

		} catch (Exception e) {
			logger.warn("MongoDB props not properly loaded. Default values are used.", e);
		}
		
	}
	
	@Produces
	@ApplicationScoped
	public MongoClient getClient() {
		String host = mongoprops.getProperty(PROP_HOST, "127.0.0.1");
		int port = Integer.parseInt(mongoprops.getProperty(PROP_PORT, "27017"));
		return new MongoClient(host, port);
	}
	
	

}
