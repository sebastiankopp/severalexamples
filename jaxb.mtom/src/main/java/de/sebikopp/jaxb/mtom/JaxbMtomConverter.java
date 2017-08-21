package de.sebikopp.jaxb.mtom;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class JaxbMtomConverter {
	private final ConcurrentHashMap<Class<?>, JAXBContext> ctx =  new ConcurrentHashMap<>();
	public <T> JaxbMtomContainer encodeDataFromXmlRootObj(T input, Class<T> rootClass) throws JAXBException, ParserConfigurationException {
		return marshal(input, getJaxbCtx(rootClass));
	}
	
	public <T> JaxbMtomContainer encodeDataFromJaxbElem(JAXBElement<T> data, Optional<String> p2schema)
			throws JAXBException, ParserConfigurationException, SAXException, IOException {
		Class<T> declaredType = data.getDeclaredType();
		final JAXBContext jaxbCtx = getJaxbCtx(declaredType);
		if (p2schema.isPresent()) {
			new MtomValidator<>(jaxbCtx, p2schema.get()).validate(data);
		}
		return marshal(data, jaxbCtx);
	}
	
	private JaxbMtomContainer marshal(Object input, JAXBContext ctx) throws JAXBException, ParserConfigurationException {
		final Marshaller mar = ctx.createMarshaller();
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		final MtomAttachmentMarshaller mtomAttachmentMarshaller = new MtomAttachmentMarshaller();
		mar.setAttachmentMarshaller(mtomAttachmentMarshaller);
		mar.marshal(input, doc);
		Map<String, byte[]> containedData = mtomAttachmentMarshaller.getContainedData();
		return new JaxbMtomContainer(doc.getDocumentElement(), containedData);
	}
	
	public <T> T unmarshal(JaxbMtomContainer container, Class<T> clazz, Optional<String> p2schema) throws JAXBException, SAXException, IOException {
		final JAXBContext jaxbCtx = getJaxbCtx(clazz);
		Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
		unmarshaller.setAttachmentUnmarshaller(new MtomAttachmentUnmarshaller(container));
		JAXBElement<T> unmarshalledElement = unmarshaller.unmarshal(container.getXmlTree(), clazz);
		if (p2schema.isPresent()) {
			new MtomValidator<>(jaxbCtx, p2schema.get()).validate(unmarshalledElement);
		}
		return unmarshalledElement.getValue();
	}
	
	private JAXBContext getJaxbCtx(Class<?> cls) {
		return ctx.computeIfAbsent(cls, clazz -> {
			try {
				return JAXBContext.newInstance(clazz);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}
	
}
