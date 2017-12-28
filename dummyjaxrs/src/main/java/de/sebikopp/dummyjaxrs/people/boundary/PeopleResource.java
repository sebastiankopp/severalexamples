package de.sebikopp.dummyjaxrs.people.boundary;

import java.util.UUID;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import de.sebikopp.dummyjaxrs.boundary.JsonCollectors;
import de.sebikopp.dummyjaxrs.people.control.PeopleStore;
import de.sebikopp.dummyjaxrs.people.entity.Person;

@Path("persons")
public class PeopleResource {

	@Inject
	PeopleStore store;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public  JsonArray getAll() {
		return store.getAll().stream()
				.map(PeopleConverter::personToJson)
				.collect(JsonCollectors.objColl());
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject getPerson(@PathParam("id") @NotNull String id) {
		return PeopleConverter.personToJson(store.getPerson(uuidFromClientRequest(id.trim())));
	}

	@DELETE
	@Path("{id}")
	public Response deletePerson(@PathParam("id") @NotNull String id) {
		UUID pid = uuidFromClientRequest(id.trim());
		store.deletePerson(pid);
		return Response.noContent().build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPerson(JsonObject req) {
		Person newPers = PeopleConverter.personFromJson(req);
		UUID persUUid = store.addPerson(newPers);
		return Response.created(UriBuilder
				.fromResource(PeopleResource.class)
				.path(persUUid.toString())
				.build())
			.build();
	}
	
	private UUID uuidFromClientRequest(String id) {
		try {
			return UUID.fromString(id);
		} catch (Exception e) {
			throw new BadRequestException(e);
		}
	}
}
