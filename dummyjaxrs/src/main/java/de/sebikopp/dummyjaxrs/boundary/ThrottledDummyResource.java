package de.sebikopp.dummyjaxrs.boundary;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.apache.logging.log4j.Logger;

import com.airhacks.porcupine.execution.boundary.Dedicated;

import de.sebikopp.dummyjaxrs.CustomThreadPoolConfig;
import de.sebikopp.dummyjaxrs.control.SlowService;

@Path("thrdummy")
@ApplicationScoped
public class ThrottledDummyResource {
	
	@Inject
	@Dedicated(CustomThreadPoolConfig.PIPELINE_THROTTLED)
	ExecutorService executorService;

	@Inject
	SlowService service;
	
	@Inject
	Logger logger;
	
	@GET
	public void processSth(@Suspended AsyncResponse response) {
		logger.info("Executor service ({}) info: {}", executorService.getClass(), executorService);
		CompletableFuture.supplyAsync(service::giveMeSomeRandom, executorService)
				.thenAccept(response::resume);
	}

}
