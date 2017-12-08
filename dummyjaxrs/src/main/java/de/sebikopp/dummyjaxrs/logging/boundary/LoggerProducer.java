package de.sebikopp.dummyjaxrs.logging.boundary;

import java.util.Optional;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.logging.log4j.Logger;

public class LoggerProducer {
	
	/**
	 * The selection of the logger to inject is solved using a 'classical' Java-SE based 
	 * annotation evaluation instead of using qualifiers. Nevertheless, {@link CustomLogger}
	 * is a valid qualifier annotation for being used to get the desired logger.
	 * 
	 * @param injp the {@link InjectionPoint} the logger is injected into
	 * @return the desired {@link Logger}
	 */
	@Produces
	@CustomLogger
	@Default
	public Logger getCustomizedLogger(final InjectionPoint injp) {
		return Optional.ofNullable(injp)
				.map(InjectionPoint::getAnnotated)
				.map(x -> x.getAnnotation(CustomLogger.class))
				.map(CustomLogger::value)
				.orElse(LoggerName.DEFAULT_LOGGER)
				.getAssignedLogger();
	}
	
}
