package de.sebikopp.dummyjaxrs;

import java.time.format.DateTimeFormatter;

public interface ConstantValues {
	
	public static final DateTimeFormatter COMMON_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final DateTimeFormatter COOMON_TSTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZZZZZ");

}
