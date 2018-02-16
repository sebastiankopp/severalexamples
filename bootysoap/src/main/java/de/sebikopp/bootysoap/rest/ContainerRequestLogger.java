package de.sebikopp.bootysoap.rest;

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

	@Autowired
	Logger logger;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		logger.info("Encountered some request on {}", () -> requestContext.getUriInfo().getRequestUri());
		logger.debug("Http Headers: {}", requestContext.getHeaders());
	}

}
