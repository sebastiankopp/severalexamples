package de.sebastiankopp.severalexamples.dummyjaxrs.boundary;

import java.io.StringReader;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import de.sebastiankopp.severalexamples.dummyjaxrs.logging.boundary.CustomLogger;
import de.sebastiankopp.severalexamples.dummyjaxrs.logging.boundary.LoggerName;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * Validator for XML using Bean Validation (JSR 349), e.g. in a JAX-RS context. In this example, the schema has to be included in the classpath.
 * @author sebi
 *
 */
public class XmlBeanValidationHandler implements ConstraintValidator<ValidXml, String> {

	private Validator schemaValidator = null;

	@Inject
	@CustomLogger(LoggerName.VALIDATION_LOGGER)
	private Logger validationLogger;
	
	private boolean isEnabled = false;
	
	@Override
	public void initialize(ValidXml constraintAnnotation) {
		try {
			String p2Schema = constraintAnnotation.pathToSchema();
			ClassLoader cl = getClass().getClassLoader();
			Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
					.newSchema(new StreamSource(cl.getResourceAsStream(p2Schema)));
			schemaValidator = schema.newValidator();
			isEnabled = true;
		} catch (Exception e) {
			validationLogger.log(Level.ERROR, "Could not initialize XML auto-validation due to a fatal error.", e);
			isEnabled = false;
		}
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (!isEnabled) {
			validationLogger.log(Level.WARN, "Could not validate XML hence an initialzation error occured. XML will be passed without validating it.");
			validationLogger.log(Level.INFO, "Non-validated XML: " + value);
			return true;
		}

		try {
			if (value == null ) {
				throw new SAXException("Empty XML");
			}
			Source xmlSrc = new StreamSource(new StringReader(value));
			schemaValidator.validate(xmlSrc);
		} catch (SAXException e) {
			validationLogger.log(Level.WARN, "Invalid XML: " + value);
			validationLogger.log(Level.INFO, "Reason for invalidity: " + e.getMessage());
			return false;
		} catch (Exception e) {
			validationLogger.log(Level.ERROR, "XML could not be validated due to a runtime error. It will be passed without validating it.", e);
			validationLogger.log(Level.INFO, "Non-validated XML: " + value);
		}
		return true;
	}

}
