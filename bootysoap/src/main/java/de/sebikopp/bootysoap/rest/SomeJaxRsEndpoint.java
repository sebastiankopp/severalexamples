package de.sebikopp.bootysoap.rest;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("junk")
public class SomeJaxRsEndpoint {

	@Autowired
	Executor executor;
	
	@Autowired
	SomeInjectedDependency jnk;
	
	@GET
	public String sth(@Context HttpHeaders headers) {
		return jnk.gimmeSth() + Objects.toString(headers.getRequestHeaders());
	}
	
	@GET
	@Path("slooow")
	public void sthInHyperspeed(@Context HttpHeaders headers, @Suspended AsyncResponse asyncResponse) {
		asyncResponse.setTimeout(3, TimeUnit.SECONDS);
		CompletableFuture.supplyAsync(jnk::gimmeSomethingInLudicrousSpeed, executor)
				.thenApplyAsync(e -> e + Objects.toString(headers.getRequestHeaders()), executor)
				.thenAccept(asyncResponse::resume);
	}
	
}
