package de.sebikopp.dummyjaxrs.control;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SlowService {
	
	public String giveMeSomeRandom() {
		try {
			TimeUnit.SECONDS.sleep(2L);
		} catch (InterruptedException e) {
		}
		return UUID.randomUUID().toString();
	}

}
