package de.sebikopp.dummyjaxrs.logging.boundary;

import java.util.Optional;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerProducer {
	
	@Produces
	public Logger getCustomizedLogger(final InjectionPoint injp) {
		return Optional.ofNullable(injp.getAnnotated().getAnnotation(CustomLogger.class))
				.map(CustomLogger::value)
				.map(LoggerName::getLoggerName)
				.map(LogManager::getLogger)
			.orElseGet(() -> LogManager.getLogger(LoggerName.DEFAULT_LOGGER.getLoggerName()));
	}
}
