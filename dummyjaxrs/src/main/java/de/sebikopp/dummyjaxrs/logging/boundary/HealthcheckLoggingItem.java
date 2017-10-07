package de.sebikopp.dummyjaxrs.logging.boundary;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

@Startup
@Singleton
public class HealthcheckLoggingItem {

	@Resource
	ManagedScheduledExecutorService mses;
	
	@Resource
	ManagedExecutorService mes;
	
	@Inject
	Logger lggr;
	
	@PostConstruct
	private void init() {
		this.mses.scheduleAtFixedRate(this::exec, 0, 10, TimeUnit.SECONDS);
	}
	
	void exec() {
		lggr.info("System is alive");
		try {
			CompletableFuture<String> cfut = CompletableFuture.supplyAsync(() -> UUID.randomUUID().toString(), mes)
					.thenApplyAsync(String::chars, mes)
					.thenApplyAsync(IntStream::sorted, mes)
					.thenApplyAsync(x -> x.boxed().map(Integer::toHexString).collect(Collectors.joining()), mes);
			
			CompletableFuture<String> cfut2 = CompletableFuture.supplyAsync(() -> UUID.randomUUID(), mes)
					.thenApplyAsync(UUID::toString, mes);
			cfut.thenCombineAsync(cfut2, (a,b) -> {
				return a + b;
			},  mes);
			lggr.info("Array of nonsense: {}", cfut.get());
		} catch (Exception e) {
			lggr.info("A fatal exec error has occured", e);
		}
	}
	
	
	
}
