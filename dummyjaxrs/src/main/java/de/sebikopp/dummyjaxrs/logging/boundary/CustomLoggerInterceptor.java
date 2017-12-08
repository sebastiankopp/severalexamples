package de.sebikopp.dummyjaxrs.logging.boundary;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.apache.logging.log4j.Logger;

@Interceptor
@Logged
@Priority(Interceptor.Priority.APPLICATION)
public class CustomLoggerInterceptor {

	@AroundInvoke
	public Object aroundInvoke(InvocationContext ic) throws Exception {
		Logger logger = extractInterceptorAnnotation(ic)
				.map(ann -> ann.logger())
				.orElse(LoggerName.DEFAULT_LOGGER)
				.getAssignedLogger();
		try {
			Instant startTs = Instant.now();
			logger.debug("Entering method {} of class {} with params {}",
					() -> ic.getMethod().getName(),
					() -> ic.getTarget().getClass(),
					() -> ic.getParameters());
			Object rc = ic.proceed();
			Instant end = Instant.now();
			logger.debug("Existing method {} of class {} which returned value {} and took {} ms to proceed.",
					() -> ic.getMethod().getName(),
					() -> ic.getTarget().getClass(),
					() -> rc,
					() -> Duration.between(startTs, end).toMillis());
			return rc;
		} catch (Exception e) {
			logger.error("An error occured while processing", e);
			throw e;
		}
		
	}
	
	private Optional<Logged> extractInterceptorAnnotation(InvocationContext context) {
		Optional<Logged> rc = Optional.ofNullable(context.getMethod().getAnnotation(Logged.class));
		if (rc.isPresent()) {
			return rc;
		}
		rc = Optional.ofNullable(context.getTarget().getClass().getAnnotation(Logged.class));
		return rc;
		
	}

}
