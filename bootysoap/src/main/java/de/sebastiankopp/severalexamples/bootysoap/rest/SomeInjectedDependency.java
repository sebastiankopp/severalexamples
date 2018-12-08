package de.sebastiankopp.severalexamples.bootysoap.rest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class SomeInjectedDependency {

	public String gimmeSth() {
		return UUID.randomUUID().toString();
	}
	
	public String gimmeSomethingInLudicrousSpeed() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
		return "---" + UUID.randomUUID();
	}
}
