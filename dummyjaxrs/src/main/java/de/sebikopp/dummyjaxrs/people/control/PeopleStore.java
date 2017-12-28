package de.sebikopp.dummyjaxrs.people.control;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import de.sebikopp.dummyjaxrs.logging.boundary.Logged;
import de.sebikopp.dummyjaxrs.people.entity.Person;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Logged
public class PeopleStore {
	private final ConcurrentMap<UUID,Person> people = new ConcurrentHashMap<>(); 

	public UUID addPerson(Person p) {
		UUID persId = p.getId();
		if (people.containsKey(persId)) {
			throw new BadRequestException("Creating a person with existing ID is prohibited");
		}
		people.put(persId, p);
		return persId;
	}
	
	public void deletePerson (UUID pId) {
		if (!people.containsKey(pId)) {
			throw new NotFoundException("Person with ID " + pId + " was not found");
		}
		people.remove(pId);
	}
	
	public Person getPerson(UUID pId) {
		if (!people.containsKey(pId)) {
			throw new NotFoundException("Person with ID " + pId + " was not found");
		}
		return people.get(pId);
	}
	
	public Collection<Person> getAll() {
		return people.values();
	}
}
