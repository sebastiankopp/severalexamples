package de.sebastiankopp.severalexamples.dummyjaxrs.boundary;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * Demonstrates handling exeptions in JAX-RS
 * @author sebi
 *
 */
@Provider
public class ExceptionFilter implements ExceptionMapper<RuntimeException> {

	@Inject
	Logger lggr;
	
	@Override
	public Response toResponse(RuntimeException exception) {
		lggr.log(Level.ERROR, "A potentially problematic error occured", exception);
		return Response.status(getStatus(exception))
				.entity(exception.getMessage())
				.type(MediaType.TEXT_PLAIN)
				.build();
	}
	
	int getStatus (RuntimeException exc) {
		return (exc instanceof WebApplicationException) ?
				((WebApplicationException) exc).getResponse().getStatus() : Status.INTERNAL_SERVER_ERROR.getStatusCode();
	}

}
