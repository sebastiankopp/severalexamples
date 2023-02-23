package de.sebastiankopp.severalexamples.bootysoap.soap;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import jakarta.activation.DataHandler;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SoapLogHandler implements SOAPHandler<SOAPMessageContext> {

	@Autowired
	Logger logger;
	
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		try {
			if (context.getMessage().getSOAPBody().hasFault()) {
				return handleFault(context);
			}
			logger.debug("SoapMessage: {}", () -> writeSoapMessage(context));
		} catch (SOAPException e) {
			logger.error("Unexpected exception while handling a message", e);
		}
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		logger.error("A fault was detected during handling a soap message: {}", () -> writeSoapMessage(context));
		return true;
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	String writeSoapMessage(SOAPMessageContext msgCtx) {
		Map<String, DataHandler> attachments = getMtomAttachments(msgCtx);
		if (attachments.isEmpty()) {
			return msgFullToString(msgCtx);
		} else {
			final SOAPMessage message = msgCtx.getMessage();
			try {
				return DomHelper.domNodeToString(message.getSOAPPart()) + "; Attachments: " + attachments;
			} catch (Exception e) {
				logger.warn("Could not write message {} with attachments {} due to an exception",
						message, attachments, e);
				return "++ NO MESSAGE LOGGED ++";
			}
		}
				
	}
	
	String msgFullToString(SOAPMessageContext msgCtx) {
		final SOAPMessage message = msgCtx.getMessage();
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream(); 
			message.writeTo(os);
			String cs = (String) message.getProperty(SOAPMessage.CHARACTER_SET_ENCODING);
			Charset msgCs = cs != null ? Charset.forName(cs) : StandardCharsets.UTF_8;
			return new String(os.toByteArray(), msgCs);
		} catch (Exception e) {
			logger.warn("Could not write message {} due to an exception", message, e);
			return "++ NO MESSAGE LOGGED ++";
		}
	}
	
	@SuppressWarnings("unchecked")
	Map<String,DataHandler> getMtomAttachments(SOAPMessageContext context) {
		return (Map<String,DataHandler>) context.getOrDefault(
				isOutbound(context) ? MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS : MessageContext.INBOUND_MESSAGE_ATTACHMENTS,
				Collections.<String,DataHandler>emptyMap());
	}
	
	boolean isOutbound (MessageContext context) {
		return (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
	}
}
