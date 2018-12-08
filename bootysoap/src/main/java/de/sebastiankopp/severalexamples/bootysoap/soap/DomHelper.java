package de.sebastiankopp.severalexamples.bootysoap.soap;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

public class DomHelper {

	public static String domNodeToString(Node n) throws TransformerException, IOException {
		try (final StringWriter sw = new StringWriter()) {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			StreamResult result = new StreamResult(sw);
			transformer.transform(new DOMSource(n), result);
			return result.toString();
		}
	}
}
