package de.sebikopp.dummyjaxrs.people.boundary;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import de.sebikopp.dummyjaxrs.ConstantValues;
import de.sebikopp.dummyjaxrs.boundary.JsonCollectors;
import de.sebikopp.dummyjaxrs.people.control.ClientCausedException;
import de.sebikopp.dummyjaxrs.people.entity.Person;

class PeopleConverter {
	private static final String JSON_KEY_PHONE_NUMBERS = "phoneNumbers";
	private static final String JSON_KEY_E_MAIL = "eMail";
	private static final String JSON_KEY_DATE_OF_BIRTH = "dateOfBirth";
	private static final String JSON_KEY_LAST_NAME = "lastName";
	private static final String JSON_KEY_FIRST_NAME = "firstName";
	private static final String JSON_KEY_ID = "id";

	static JsonObject personToJson(Person p) {
		Objects.requireNonNull(p, "An object to be stringified may not be null");
		return Json.createObjectBuilder()
			.add(JSON_KEY_ID, p.getId().toString())
			.add(JSON_KEY_FIRST_NAME, p.getFirstName())
			.add(JSON_KEY_LAST_NAME, p.getLastName())
			.add(JSON_KEY_DATE_OF_BIRTH, p.getDateOfBirth().format(ConstantValues.COMMON_DATE_FORMAT))
			// reflexiver Sinnlos-Cast, damit der Eclipsecompiler nicht meckert
			.add(JSON_KEY_E_MAIL, (JsonArray) p.geteMail().stream().collect(JsonCollectors.collectStrings()))
			.add(JSON_KEY_PHONE_NUMBERS, (JsonArray) p.getPhoneNumbers().stream().collect(JsonCollectors.collectStrings()))
		.build();
	}
	
	static Person personFromJson(JsonObject obj) {
		String id = obj.getString(JSON_KEY_ID, null);
		if (!obj.containsKey(JSON_KEY_FIRST_NAME)) {
			throw new ClientCausedException("First name is missing");
		}
		String fName = obj.getString(JSON_KEY_FIRST_NAME);
		if (!obj.containsKey(JSON_KEY_LAST_NAME)) {
			throw new ClientCausedException("Last name is missing");
		}
		String lName = obj.getString(JSON_KEY_LAST_NAME);
		if (!obj.containsKey(JSON_KEY_DATE_OF_BIRTH)) {
			throw new ClientCausedException("Date of birth is missing");
		}
		String bday = obj.getString(JSON_KEY_DATE_OF_BIRTH);
		LocalDate bdayDate = null;
		try {
			bdayDate = LocalDate.parse(bday, ConstantValues.COMMON_DATE_FORMAT);
		} catch (Exception e1) {
			throw new ClientCausedException(e1);
		}
		
		Set<String> eMails = Collections.emptySet();
		if (obj.containsKey(JSON_KEY_E_MAIL)) {
			JsonArray arrMail = obj.getJsonArray(JSON_KEY_E_MAIL);
			eMails = arrMail.stream()
					.filter(e -> (e instanceof JsonString))
					.map(JsonValue::toString)
					.collect(Collectors.toSet());
		}
		Set<String> pNmbrs = Collections.emptySet();
		if (obj.containsKey(JSON_KEY_PHONE_NUMBERS)) {
			JsonArray arrPhone = obj.getJsonArray(JSON_KEY_PHONE_NUMBERS);
			pNmbrs = arrPhone.stream()
					.filter(e -> (e instanceof JsonString))
					.map(JsonValue::toString)
					.collect(Collectors.toSet());
		}
		return new Person(id, fName, lName, bdayDate, eMails, pNmbrs);
	}
	

}
