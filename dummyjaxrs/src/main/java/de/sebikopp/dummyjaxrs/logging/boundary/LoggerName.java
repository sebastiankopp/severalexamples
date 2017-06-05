package de.sebikopp.dummyjaxrs.logging.boundary;

public enum LoggerName {
	VALIDATION_LOGGER("validationLogger"),
	DEFAULT_LOGGER("defaultLogger");
	
	private final String loggerName;
	
	private LoggerName(String loggerName) {
		this.loggerName = loggerName;
	}
	
	public String getLoggerName() {
		return loggerName;
	}
}
