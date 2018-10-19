package de.sebikopp.bootysoap.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.sebikopp.bootysoap.batch.control.JdbcPeopleReader;
import de.sebikopp.bootysoap.batch.entity.Person;

@Component
@Path("people")
@RequestScope
public class PeopleResource {

	@Autowired
	JdbcPeopleReader pread;
	
	@GET
	@Produces(APPLICATION_JSON)
	public Response getAll() throws JsonProcessingException {
		List<Person> people = pread.getAllPeople();
		String valueAsString = new ObjectMapper().writeValueAsString(people);
		return Response.ok(valueAsString).build();
	}
}
