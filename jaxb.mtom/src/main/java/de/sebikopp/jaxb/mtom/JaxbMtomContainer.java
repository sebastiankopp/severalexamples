package de.sebikopp.jaxb.mtom;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class JaxbMtomContainer {
	private final Element xmlTree;
	private final Map<String,byte[]> attachments;
	public JaxbMtomContainer(Element xmlTree, Map<String, byte[]> attachments) {
		super();
		this.xmlTree = xmlTree;
		this.attachments = attachments;
	}
	public Element getXmlTree() {
		return xmlTree;
	}
	public Map<String, byte[]> getAttachments() {
		return attachments;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attachments == null) ? 0 : attachments.hashCode());
		result = prime * result + ((xmlTree == null) ? 0 : xmlTree.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JaxbMtomContainer other = (JaxbMtomContainer) obj;
		if (attachments == null) {
			if (other.attachments != null)
				return false;
		} else if (!attachments.equals(other.attachments))
			return false;
		if (xmlTree == null) {
			if (other.xmlTree != null)
				return false;
		} else if (!xmlTree.equals(other.xmlTree))
			return false;
		return true;
	}
	@Override
	public String toString() {
		String xmlOutput = null;
		try {
			xmlOutput = getStringFomXmlNode(xmlTree);
		} catch (Exception e) {
			xmlOutput = Objects.toString(xmlTree);
		}
		return "JaxbMtomContainer [xmlTree=" + xmlOutput + ", attachments=" + attachments + "]";
	}
	
	static String getStringFomXmlNode(Node n) throws TransformerException, IOException {
		DOMSource domSource = new DOMSource(n);
		try (final StringWriter sw = new StringWriter()){
			StreamResult sr = new StreamResult(sw);
			TransformerFactory.newInstance().newTransformer().transform(domSource, sr);
			return sw.toString();
		}
	}
}
