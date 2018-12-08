package de.sebastiankopp.severalexamples.dummyjaxrs.boundary;

import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target({FIELD, METHOD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = XmlBeanValidationHandler.class)
@Documented
public @interface ValidXml {
	String message() default "Invalid Xml";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    String pathToSchema();
}
