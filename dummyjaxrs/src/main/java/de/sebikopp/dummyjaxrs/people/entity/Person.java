package de.sebikopp.dummyjaxrs.people.entity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Person {
	private final UUID id;
	private final String firstName;
	private final String lastName;
	private final LocalDate dateOfBirth;
	private final Set<String> eMail;
	private final Set<String> phoneNumbers;
	
	public Person(String id, String firstName, String lastName, LocalDate dateOfBirth, Set<String> eMail, Set<String> phoneNumbers) {
		super();
		this.id = Optional.ofNullable(id)
				.filter(e -> !e.isEmpty())
				.map(UUID::fromString)
				.orElseGet(UUID::randomUUID);
		Objects.requireNonNull(this.firstName = firstName, "First name is null");
		Objects.requireNonNull(this.lastName = lastName, "Last name is null");
		Objects.requireNonNull(this.dateOfBirth = dateOfBirth, "Date of birth is null");
		this.eMail = Stream.of(eMail)
				.filter(e -> e != null)
				.flatMap(Set::stream)
				.filter(e -> e != null && !e.isEmpty())
				.collect(Collectors.toSet());
		this.phoneNumbers = Stream.of(phoneNumbers)
				.filter(e -> e != null)
				.flatMap(Set::stream)
				.filter(e -> e != null && !e.isEmpty())
				.collect(Collectors.toSet());
	}
	public UUID getId() {
		return id;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public Set<String> geteMail() {
		return eMail;
	}
	public Set<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	
	
}
