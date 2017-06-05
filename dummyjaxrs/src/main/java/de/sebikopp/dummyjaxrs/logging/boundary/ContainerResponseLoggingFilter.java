package de.sebikopp.dummyjaxrs.logging.boundary;

import java.io.IOException;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import de.sebikopp.dummyjaxrs.boundary.JsonCollectors;

@Provider
public class ContainerResponseLoggingFilter implements ContainerResponseFilter {
	
	@Inject
	Logger lggr;

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		if (lggr.isEnabled(Level.INFO)) {
			lggr.info("HTTP-Resonse from " + requestContext.getUriInfo().getAbsolutePath() + " with status code " + responseContext.getStatus());
			JsonObjectBuilder hdrJsonFormatter = Json.createObjectBuilder();
			responseContext.getHeaders().forEach((k,v) -> {
				JsonArray valList = v.stream()
						.map(Object::toString)
						.collect(JsonCollectors.collectStrings());
				hdrJsonFormatter.add(k, valList);
			});
			lggr.info("Response headers: " + hdrJsonFormatter.build());
			if (responseContext.hasEntity() && lggr.isEnabled(Level.DEBUG)) {
//				Type entityType = responseContext.getEntityType();
				lggr.debug("Response entity: " + responseContext.getEntity());
				
			}
		}
		
	}
	
	

}
