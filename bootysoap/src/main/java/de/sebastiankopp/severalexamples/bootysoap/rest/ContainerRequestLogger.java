package de.sebastiankopp.severalexamples.bootysoap.rest;

import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Provider
@Component
public class ContainerRequestLogger implements ContainerRequestFilter {

	private static final String SPECIAL_HEADER = "XX-VerySpecialInternalCallId";
	@Autowired
	Logger logger;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		logger.info("Encountered some request on {}", () -> requestContext.getUriInfo().getRequestUri());
		requestContext.getHeaders().putIfAbsent(SPECIAL_HEADER, asList(randomUUID().toString()));
		logger.debug("Http Headers: {}", requestContext.getHeaders());
	}

}
