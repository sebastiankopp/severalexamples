package de.sebikopp.dummyjaxrs.boundary;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import de.sebikopp.dummyjaxrs.people.control.ClientCausedException;

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
		Status status = Status.INTERNAL_SERVER_ERROR;
		if (exception instanceof ClientCausedException) {
			ClientCausedException exc = (ClientCausedException) exception;
			switch (exc.getType()) {
			case NOT_FOUND:
				status = Status.NOT_FOUND;
			default:
				status = Status.BAD_REQUEST;
			}
		}
		return new WebApplicationException(exception, status).getResponse();
	}

}
