package de.sebastiankopp.severalexamples.bootysoap.batch.entity;

import java.util.Objects;

public class Person {
	private String firstName;
	private String lastName;
	
	protected Person() {
	}

	public Person(String firstName, String lastName) {
		super();
		this.firstName = Objects.requireNonNull(firstName);
		this.lastName = Objects.requireNonNull(lastName);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName + "]";
	}
}
