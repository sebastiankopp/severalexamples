package de.sebastiankopp.severalexamples.dummyjaxrs.people.boundary;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.sebastiankopp.severalexamples.dummyjaxrs.boundary.ValidXml;


/**
 * Am example for a JAX-RS resource with integrated XML validation.
 * 
 * @author sebi
 *
 */
@Path("peoplexml")
public class PeopleXmlResource {
	
	@GET
	public Response healthCheck() {
		return Response.noContent().build();
	}
	
	/**
	 * This endpoint method is not intended to store people!
	 * @param xml
	 * @return
	 */
	@POST
	@Consumes (MediaType.APPLICATION_XML)
	public Response consume(@ValidXml(pathToSchema="xsd/people.xsd") String xml) {
		return Response.noContent().build();
	}

}
