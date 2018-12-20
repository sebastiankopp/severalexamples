package de.sebastiankopp.jaxb.mtom;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 * Additional validator for MTOM-based content in order to filter out most common types of invalidity.
 * In general, there are several kinds of XML constraint violations always filtered out by JAXB, for instance: 
 * <ul>
 * 	<li>Not well-wormed XML.</li>
 * 	<li>Illegal values of XML enumerations bound to java enums.</li>
 * 	<li>Illegal element names &amp; orders.</li>
 * </ul>
 * 
 * But several restrictions are only checked by JAXB if a schema is specified (the classical variant of this way is not applicable 
 * when using MTOM since these attachments are defined as <code>base64Binary</code> in schemas (simple type) but are bound as 
 * <code>xop:Include</code> elements in schema instances -- so they are no simple content anymore. This requires another way of validation
 * hence I use the validation of JAXB sources here.
 * 
 * So this validator provides following XML validation features:
 * <ul>
 * 	<li>Validation against string/token patterns.</li>
 * 	<li>Validation of enumerations without a typesafe java binding.</li>
 * 	<li>Validation of constraints like <code>unique</code>, <code>key</code> and <code>keyref</code>
 * </ul>
 *  
 * @author sebi
 *
 * @param <T> Type of root element which has to be validated -- only if its type is specified via the constructor.
 */
public class MtomValidator<T> {
	private final JAXBContext jc;
	private final Validator validator;
	public MtomValidator(Class<T> jaxbRootClass, String p2Schema) throws JAXBException, SAXException{
		this.jc = JAXBContext.newInstance(jaxbRootClass);
		this.validator = createValidator(p2Schema);
	}

	public MtomValidator(JAXBContext ctx, String p2schema) throws SAXException {
		this.jc = ctx;
		this.validator = createValidator(p2schema); 
	}
	
	public MtomValidator(Class<T> rootClass, Schema schema) throws JAXBException, SAXException {
		this.jc = JAXBContext.newInstance(rootClass);
		this.validator = schema.newValidator();
	}
	
	public MtomValidator(JAXBContext ctx, Schema schema) throws JAXBException, SAXException {
		this.jc = ctx;
		this.validator = schema.newValidator();
	}
	
	private Validator createValidator(String p2Schema) throws SAXException {
		return schemaFromCpPath(p2Schema).newValidator();
	}

	public static Schema schemaFromCpPath(String p2Schema) {
		try {
			return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
					.newSchema(new StreamSource(MtomValidator.class.getClassLoader().getResourceAsStream(p2Schema)));
		} catch (SAXException e) {
			// Exceptions which are not assignable to RuntimeExceptions shall not be thrown => easier use in lambdas and method references
			throw new RuntimeException(e);
		}
	}
	
	public <U extends T> void validate(final U object) throws JAXBException, SAXException, IOException {
		JAXBSource src = new JAXBSource(jc, object);
		validator.validate(src);
	}
}
