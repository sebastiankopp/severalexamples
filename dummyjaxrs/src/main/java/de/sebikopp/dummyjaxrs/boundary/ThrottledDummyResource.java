package de.sebikopp.dummyjaxrs.boundary;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import com.airhacks.porcupine.execution.boundary.Dedicated;

import de.sebikopp.dummyjaxrs.CustomThreadPoolConfig;
import de.sebikopp.dummyjaxrs.control.SlowService;

@Path("thrdummy")
public class ThrottledDummyResource {
	
	@Inject
	@Dedicated(CustomThreadPoolConfig.PIPELINE_THROTTLED)
	ExecutorService executorService;

	@Inject
	SlowService service;
	
	@GET
	public void processSth(@Suspended AsyncResponse response) {
		executorService.execute(() -> {
			String someRandom = service.giveMeSomeRandom();
			response.resume(someRandom);
		});
	}

}
