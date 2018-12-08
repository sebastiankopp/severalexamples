package de.sebastiankopp.severalexamples.bootysoap.jsf.boundary;

import static java.time.Instant.now;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("view")
public class TheExample {

	public String getTsInfo() {
		return now().toString();
	}

	public String getHostInfo() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "Unknown Host Problem: " + e.getMessage();
		}
	}
}
