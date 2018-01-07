package de.sebikopp.dummyjaxrs.boundary;

import java.lang.reflect.Field;
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

import de.sebikopp.dummyjaxrs.RegexMatchers;
import de.sebikopp.dummyjaxrs.control.SlowService;

public class ThrottlerIT {
	
	private static final String URI_OVERLOAD_RES = "http://localhost:8080/dummyjaxrs/resources/thrdummy";
//	private static final String URI_STATS_RES = "http://localhost:8080/dummyjaxrs/resources/statistics";

	@Test
	public void test() throws Exception {
		WebTarget target = createTarget(URI_OVERLOAD_RES);
		int count = 12;
		final int poolSize = 4;
		final Duration minDuration = Duration.ofSeconds((count/poolSize)*getSleepDuration());
		
		List<Future<String>> futures = new ArrayList<>();
		
		for (int i = 0; i < count; i++) {
			futures.add(target.request().async().get(String.class));
		}
		Instant registeredStart = Instant.now();
		for (Future<String>  future: futures) {
			String string = future.get();
			Assert.assertThat(string, RegexMatchers.isUuid());
		}
		Instant end = Instant.now();
		Duration processingTime = Duration.between(registeredStart, end);
		Assert.assertThat(processingTime, Matchers.greaterThanOrEqualTo(minDuration));
	}

	private int getSleepDuration() throws Exception {
		Field field = SlowService.class.getDeclaredField("SLEEP_DURATION");
		try {
			field.setAccessible(true);
			return field.getInt(null);
		} finally {
			field.setAccessible(false);
		}
	}
	
	private WebTarget createTarget(String uri) {
		WebTarget target = ClientBuilder.newClient()
				.target(uri);
		return target;
	}
	

}
