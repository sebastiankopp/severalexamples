package de.sebastiankopp.severalexamples.bootysoap.rest;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Component
@Path("junk")
@RequestScope
public class SomeJaxRsEndpoint {

	@Autowired
	Executor executor;
	
	@Autowired
	SomeInjectedDependency jnk;
	
	@Context
	HttpHeaders headers;
	
	@GET
	public String sth(@Context HttpHeaders headers) {
		return jnk.gimmeSth() + Objects.toString(headers.getRequestHeaders());
	}
		
	@GET
	@Path("heap")
	// demands header 'Accept: application/json'
	public JsonObject getHeap() {
		final Runtime runtime = Runtime.getRuntime();
		final long totalMemory = runtime.totalMemory();
		final long freeMemory = runtime.freeMemory();
		return Json.createObjectBuilder()
				.add("maxHeap", runtime.maxMemory())
				.add("totalHeap", totalMemory)
				.add("freeHeap", freeMemory)
				.add("usedHeap", totalMemory - freeMemory)
			.build();
	}
	
	@GET
	@Path("slooow")
	public void sthInHyperspeed(@Suspended AsyncResponse asyncResponse) {
		asyncResponse.setTimeout(3, TimeUnit.SECONDS);
		CompletableFuture.supplyAsync(jnk::gimmeSomethingInLudicrousSpeed, executor)
				.thenApplyAsync(e -> e + Objects.toString(headers.getRequestHeaders()), executor)
				.thenAccept(asyncResponse::resume);
	}
	
}
