package de.sebikopp.bootysoap.batch.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import de.sebikopp.bootysoap.batch.entity.Person;

@Component
public class JdbcPeopleReader {

	@Autowired
	JdbcTemplate template;
	
	public List<Person> getAllPeople() {
		return template.query("select first_name, last_name from people",
				(rs, rowNum) -> new Person(rs.getString(1), rs.getString(2)));
	}
	
}
