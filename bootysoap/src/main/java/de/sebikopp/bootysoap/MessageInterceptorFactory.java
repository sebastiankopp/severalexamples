package de.sebikopp.bootysoap;

import org.apache.cxf.phase.Phase;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import de.sebikopp.bootysoap.soap.MessageLoggingInterceptor;

@Configuration
public class MessageInterceptorFactory {

	public static final String IN_LOGGER = "inLogger";
	public static final String OUT_LOGGER = "outLogger";
	@Autowired
	Logger logger;
	
	@Bean(OUT_LOGGER)
	public MessageLoggingInterceptor getInLogger() {
		return new MessageLoggingInterceptor(Phase.PRE_UNMARSHAL) {
			@Override
			protected Logger getLogger() {
				return logger;
			}
		};
	}
	
	@Bean(IN_LOGGER)
	public MessageLoggingInterceptor getOutLogger() {
		return new MessageLoggingInterceptor(Phase.POST_MARSHAL) {
			@Override
			protected Logger getLogger() {
				return logger;
			}
		};
	}
	
}
