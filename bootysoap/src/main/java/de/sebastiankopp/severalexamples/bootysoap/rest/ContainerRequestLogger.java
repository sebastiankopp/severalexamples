package de.sebastiankopp.severalexamples.bootysoap.rest;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;

@Provider
@Component
public class ContainerRequestLogger implements ContainerRequestFilter {

	private static final String SPECIAL_HEADER = "XX-VerySpecialInternalCallId";
	@Autowired
	Logger logger;
	
	@Override
	public void filter(ContainerRequestContext requestContext) {
		logger.info("Encountered some request on {}", () -> requestContext.getUriInfo().getRequestUri());
		requestContext.getHeaders().putIfAbsent(SPECIAL_HEADER, singletonList(randomUUID().toString()));
		logger.debug("Http Headers: {}", requestContext.getHeaders());
	}

}
