package de.sebikopp.dummyjaxrs.logging.boundary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface MongoDatabaseName {

	public String value() default MongoDbProducer.DEFAULT_DB_NAME;
}
