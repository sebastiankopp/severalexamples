package de.sebastiankopp.severalexamples.dummyjaxrs;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class JaxRsResponseFilter implements ContainerResponseFilter{

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		responseContext.getHeaders().addAll("Access-Control-Allow-Origin",
				"http://localhost:8081",
				"http://localhost:8080");
		
	}

}
