package de.sebikopp.dummyjaxrs.logging.boundary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum LoggerName {
	VALIDATION_LOGGER("validationLogger"),
	DEFAULT_LOGGER("defaultLogger"),
	EXTERNAL_RES_LGGR("extResLogger");
	
	private final String loggerName;
	
	private LoggerName(String loggerName) {
		this.loggerName = loggerName;
	}
	
	public String getLoggerName() {
		return loggerName;
	}
	
	public Logger getAssignedLogger() {
		return LogManager.getLogger(loggerName);
	}
}
