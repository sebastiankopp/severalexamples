package de.sebikopp.dummyjaxrs.boundary;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class ThrottlerIT {
	
	@Test
	public void test() throws Exception {
		WebTarget target = createTarget();
		int count = 40;
		final int poolSize = 4;
		int minDuration = count/poolSize;
		
		List<Future<String>> futures = new ArrayList<>();
		
		for (int i = 0; i < 40; i++)
			futures.add(target.request().async().get(String.class));
		Instant registeredStart = Instant.now();
		for (Future<String>  future: futures) {
			future.get();
		}
		Instant end = Instant.now();
		Duration processingTime = Duration.between(registeredStart, end);
		Assert.assertThat(processingTime, Matchers.greaterThanOrEqualTo(Duration.ofSeconds(minDuration)));
	}

	private WebTarget createTarget() {
		WebTarget target = ClientBuilder.newClient()
				.target("http://localhost:8080/dummyjaxrs/resources/thrdummy");
		return target;
	}
	

}
