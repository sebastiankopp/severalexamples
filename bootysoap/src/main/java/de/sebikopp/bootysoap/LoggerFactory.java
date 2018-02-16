package de.sebikopp.bootysoap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerFactory {

	@Bean
	public Logger getDefaultLogger(InjectionPoint injp) {
		return LogManager.getLogger(injp.getMember().getDeclaringClass());
	}

}
