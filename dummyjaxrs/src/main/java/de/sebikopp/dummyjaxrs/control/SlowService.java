package de.sebikopp.dummyjaxrs.control;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.sebikopp.dummyjaxrs.logging.boundary.Logged;

public class SlowService {
	
	@Logged
	public String giveMeSomeRandom() {
		try {
			TimeUnit.SECONDS.sleep(2L);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
		return UUID.randomUUID().toString();
	}

}
