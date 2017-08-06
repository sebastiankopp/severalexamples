package de.sebikopp.dummyjaxrs.logging.boundary;

import java.util.Optional;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerProducer {
	
	/**
	 * The selection of the logger to inject is solved using a 'classical' Java-SE based 
	 * annotation evaluation instead of using qualifiers. This is less error-prone and a
	 * little bit easier and maintainable (due to the usage of enums) than the build-in
	 * CDI solution.
	 * @param injp the {@link InjectionPoint} the logger is injected into
	 * @return the desired {@link Logger}
	 */
	@Produces
	public Logger getCustomizedLogger(final InjectionPoint injp) {
		return Optional.ofNullable(injp)
				.map(InjectionPoint::getAnnotated)
				.map(x -> x.getAnnotation(CustomLogger.class))
				.map(CustomLogger::value)
				.map(LoggerName::getLoggerName)
				.map(LogManager::getLogger)
			.orElseGet(() -> LogManager.getLogger(LoggerName.DEFAULT_LOGGER.getLoggerName()));
	}
}
