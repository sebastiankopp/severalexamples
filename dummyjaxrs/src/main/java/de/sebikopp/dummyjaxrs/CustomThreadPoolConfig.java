package de.sebikopp.dummyjaxrs;

import javax.enterprise.inject.Specializes;

import com.airhacks.porcupine.configuration.control.ExecutorConfigurator;
import com.airhacks.porcupine.execution.control.ExecutorConfiguration;

@Specializes
public class CustomThreadPoolConfig extends ExecutorConfigurator {

	public static final String PIPELINE_THROTTLED = "trottled";
	
	@Override
	public ExecutorConfiguration forPipeline(String name) {
		if (PIPELINE_THROTTLED.equals(name)) {
			return new ExecutorConfiguration.Builder()
					.corePoolSize(2)
					.maxPoolSize(4)
					.discardPolicy()
					.queueCapacity(200)
				.build();
				
		}
		return super.forPipeline(name);
	}
	
}
