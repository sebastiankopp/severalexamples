package de.sebastiankopp.severalexamples.bootysoap.soap;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;

import de.sebastiankopp.severalexamples.bootysoap.wsdlx.digestwebservice.DigestWebservice;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WsConfig {

	@Autowired
	Bus bus;
	
	@Autowired
	DigestWebservice wsImpl;
	
	@Autowired
	SoapLogHandler handler;
	
	@Autowired
	Logger logger;
	
	@Bean
	public Endpoint endpoint() {
		EndpointImpl endpointImpl = new EndpointImpl(bus, wsImpl);
		logger.info("Publishing endpoint ...");
		endpointImpl.setWsdlLocation("wsdl/DigestWebservice.wsdl");
		endpointImpl.publish("/dig");
		addHandler(endpointImpl, handler);
		addHandler(endpointImpl, new HeaderAcceptor(new QName(
				"http://sebastiankopp.de/severalexamples/bootysoap/crosscuttingTypes", "CallID")));
//		completeInterceptors();
		return endpointImpl;
	}
	
	@SuppressWarnings({ "rawtypes" })
	void addHandler(EndpointImpl ep, Handler<? extends MessageContext> soapHandler) {
		List<Handler> handlerChain = ep.getBinding().getHandlerChain();
		if (handlerChain.stream().noneMatch(e -> e.getClass().equals(soapHandler.getClass()))) {
			handlerChain.add(soapHandler);
		}
		logger.info("Soap handler classes: {}", () -> handlerChain.stream()
				.map(Object::getClass).map(Class::getName).collect(Collectors.joining(", ")));
		ep.getBinding().setHandlerChain(handlerChain);
	}
	
	void completeInterceptors() {
//		CxfInterceptorChain.instrumentateBus(bus, new MessageLoggingInterceptor(true, logger), CxfInterceptorChain.IN, CxfInterceptorChain.IN_FAULT);
	}
	
}
