package de.sebastiankopp.severalexamples.bootysoap;

import java.net.URL;
import java.util.Objects;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class CustomMatchers {

	public static Matcher<String> urlStrReachable() {
		return new BaseMatcher<String>() {
			private Integer errStatus = null;
			private Exception caughtException = null;
			@Override
			public boolean matches(Object item) {
				if (item == null || !(item instanceof String))
					return false;
				final String url = (String) item;
				try {
					Response response = ClientBuilder.newClient().target(url).request().buildGet().invoke();
					errStatus = response.getStatus();
					System.out.println("Status:: " + errStatus);
					return response.getStatus() < 400;
				} catch (Exception e) {
					caughtException = e;
					return false;
				}
			}

			@Override
			public void describeTo(Description description) {
				description.appendText(" a reachable web url but the problem was ");
				if (errStatus !=null) {
					description.appendText(" an HTTP status equal to").appendValue(errStatus);
				} else if (caughtException != null) {
					description.appendText(" an exception: ").appendText(caughtException.getMessage());
				} else {
					description.appendText("  an unknown problem.");
				}
 				
			}
		};
	}
	public static Matcher<URL> urlReachable() {
		return new BaseMatcher<URL>() {
			private final Matcher<String> actualMatcher = urlStrReachable();
			@Override
			public boolean matches(Object item) {
				return item instanceof URL && actualMatcher.matches(Objects.toString(item));
			}

			@Override
			public void describeTo(Description description) {
				actualMatcher.describeTo(description);
			}
		};
	}
	
}
