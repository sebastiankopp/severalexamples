package de.sebikopp.bootysoap.rest;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

@Configuration
public class ExecutorExposer {
	@Bean
	Executor getExecutor() {
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		ConcurrentTaskExecutor executor = new ConcurrentTaskExecutor(pool);
		return executor;
	}

}
