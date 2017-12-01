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
	
	private static final String URI_OVERLOAD_RES = "http://localhost:8080/dummyjaxrs/resources/thrdummy";
//	private static final String URI_STATS_RES = "http://localhost:8080/dummyjaxrs/resources/statistics";

	@Test
	public void test() throws Exception {
		WebTarget target = createTarget(URI_OVERLOAD_RES);
		int count = 40;
		final int poolSize = 4;
		int minDuration = count/poolSize;
		
		List<Future<String>> futures = new ArrayList<>();
		
		for (int i = 0; i < count; i++) {
			futures.add(target.request().async().get(String.class));
		}
		Instant registeredStart = Instant.now();
		for (Future<String>  future: futures) {
			future.get();
		}
		Instant end = Instant.now();
		Duration processingTime = Duration.between(registeredStart, end);
		Assert.assertThat(processingTime, Matchers.greaterThanOrEqualTo(Duration.ofSeconds(minDuration)));
	}

	private WebTarget createTarget(String uri) {
		WebTarget target = ClientBuilder.newClient()
				.target(uri);
		return target;
	}
	

}
