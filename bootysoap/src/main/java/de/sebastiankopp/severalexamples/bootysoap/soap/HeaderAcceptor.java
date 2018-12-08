package de.sebastiankopp.severalexamples.bootysoap.soap;

import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class HeaderAcceptor implements SOAPHandler<SOAPMessageContext>{

	private final Set<QName> acceptedHeaders = new HashSet<>();
	public HeaderAcceptor(QName... acceptedHeaderNames) {
		stream(acceptedHeaderNames).forEachOrdered(acceptedHeaders::add);
	}
	
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {
		
	}

	@Override
	public Set<QName> getHeaders() {
		return unmodifiableSet(acceptedHeaders);
	}
	

}
