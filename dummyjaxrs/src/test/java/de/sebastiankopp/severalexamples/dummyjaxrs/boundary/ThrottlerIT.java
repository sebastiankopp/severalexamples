package de.sebastiankopp.severalexamples.dummyjaxrs.boundary;

import de.sebastiankopp.severalexamples.dummyjaxrs.test.RegexMatchers;
import de.sebastiankopp.severalexamples.dummyjaxrs.control.SlowService;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;

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
			assertThat(string, RegexMatchers.isUuid());
		}
		Instant end = Instant.now();
		Duration processingTime = Duration.between(registeredStart, end);
		assertThat(processingTime, Matchers.greaterThanOrEqualTo(minDuration));
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
