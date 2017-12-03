package de.sebikopp.bootysoap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerFactory {

	private static final String LOGGER_NAME = "MyLogger";
	@Bean
	public Logger getDefaultLogger() {
		return LogManager.getLogger(LOGGER_NAME);
	}
}
