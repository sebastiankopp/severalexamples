package de.sebikopp.dummyjaxrs.control;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.sebikopp.dummyjaxrs.logging.boundary.Logged;

public class SlowService {
	private static int SLEEP_DURATION = 2;
	@Logged
	public String giveMeSomeRandom() {
		try {
			TimeUnit.SECONDS.sleep(SLEEP_DURATION);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
		return UUID.randomUUID().toString();
	}

}
