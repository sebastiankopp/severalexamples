package de.sebikopp.jaxb.mtom;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

public class MtomValidator<T> {
	private final JAXBContext jc;
	private final Validator validator;
	public MtomValidator(Class<T> jaxbRootClass, String p2Schema) throws JAXBException, SAXException{
		this.jc = JAXBContext.newInstance(jaxbRootClass);
		this.validator = createValidator(p2Schema);
	}

	private Validator createValidator(String p2Schema) throws SAXException {
		return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
				.newSchema(new StreamSource(getClass().getClassLoader().getResourceAsStream(p2Schema)))
				.newValidator();
	}
	
	public MtomValidator(JAXBContext ctx, String p2schema) throws SAXException {
		this.jc = ctx;
		this.validator = createValidator(p2schema); 
	}

	public <U extends T> void validate(final U object) throws JAXBException, SAXException, IOException {
		JAXBSource src = new JAXBSource(jc, object);
		validator.validate(src);
	}
}
