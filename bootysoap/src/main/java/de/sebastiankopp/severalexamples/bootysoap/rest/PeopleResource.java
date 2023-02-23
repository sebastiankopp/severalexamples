package de.sebastiankopp.severalexamples.bootysoap.rest;

import java.util.List;

import de.sebastiankopp.severalexamples.bootysoap.batch.control.JdbcPeopleReader;
import de.sebastiankopp.severalexamples.bootysoap.batch.entity.Person;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

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
