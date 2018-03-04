package de.sebikopp.bootysoap.batch.control;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import de.sebikopp.bootysoap.batch.entity.Person;

@Component
public class PersonProcessor implements ItemProcessor<Person, Person>{
	
	@Override
	public Person process(Person item) throws Exception {
		return new Person(item.getFirstName().toUpperCase(), item.getLastName().toUpperCase());
	}

}
