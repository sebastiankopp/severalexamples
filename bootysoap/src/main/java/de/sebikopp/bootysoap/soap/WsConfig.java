package de.sebikopp.bootysoap.soap;

import java.util.List;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.sebikopp.bootysoap.MessageInterceptorFactory;
import de.sebikopp.bootysoap.wsdlx.digestwebservice.DigestWebservice;

@Configuration
public class WsConfig {

	@Autowired
	Bus bus;
	
	@Autowired
	DigestWebservice wsImpl;
	
	@Autowired
	@Qualifier(MessageInterceptorFactory.IN_LOGGER)
	MessageLoggingInterceptor inInterceptor;
	
	@Autowired
	@Qualifier(MessageInterceptorFactory.OUT_LOGGER)
	MessageLoggingInterceptor outInterceptor;
	
	@Bean
	public Endpoint endpoint() {
		completeInterceptors();
		EndpointImpl endpointImpl = new EndpointImpl(bus, wsImpl);
		endpointImpl.setWsdlLocation("wsdl/DigestWebservice.wsdl");
		endpointImpl.publish("/dig");
		return endpointImpl;
	}
	
	private void completeInterceptors() {
		List<Interceptor<? extends Message>> inInterceptors = bus.getInInterceptors();
		if (inInterceptors.stream().noneMatch(MessageLoggingInterceptor.class::isInstance)) {
			inInterceptors.add(inInterceptor);
		}
		List<Interceptor<? extends Message>> inFaultInterceptors = bus.getInFaultInterceptors();
		if (inFaultInterceptors.stream().noneMatch(MessageLoggingInterceptor.class::isInstance)) {
			inFaultInterceptors.add(inInterceptor);
		}
		List<Interceptor<? extends Message>> outInterceptors = bus.getOutInterceptors();
		if (outInterceptors.stream().noneMatch(MessageLoggingInterceptor.class::isInstance)) {
			outInterceptors.add(outInterceptor);
		}
		List<Interceptor<? extends Message>> outFaultInterceptors = bus.getOutFaultInterceptors();
		if (outFaultInterceptors.stream().noneMatch(MessageLoggingInterceptor.class::isInstance)) {
			outFaultInterceptors.add(outInterceptor);
		}
		
	}
}
