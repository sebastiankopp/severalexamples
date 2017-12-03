package de.sebikopp.bootysoap.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.logging.log4j.Logger;

public abstract class MessageLoggingInterceptor extends AbstractPhaseInterceptor<Message>{
	
	public MessageLoggingInterceptor(String phase) {
		super(phase);
	}
	
	@Override
	public void handleMessage(Message message) throws Fault {
		InputStream content = message.getContent(InputStream.class);
		try {
			byte[] data = extractInputstream(content);
			message.setContent(InputStream.class, new ByteArrayInputStream(Arrays.copyOf(data, data.length)));
			boolean isInbound = (boolean) message.get(Message.INBOUND_MESSAGE);
			final String msgAsString = new String(data, StandardCharsets.UTF_8);
			boolean containsFault = message.getContentFormats().stream().anyMatch(Fault.class::isAssignableFrom);
			final Logger logger = getLogger();
			final Fault fault = message.get(Fault.class);
			if (isInbound) {
				if (containsFault) {
					logger.error("Fatal error while receiving message: {}", msgAsString, fault);
				} else {
					logger.debug("Inbound Message: {}", msgAsString);
				}
			} else {
				if (containsFault) {
					logger.error("Fatal error while sending message: {}", msgAsString, fault);
				} else {
					logger.debug("Outbound message: {}", msgAsString);
				}
			}
			if (fault != null) {
				message.setContent(Fault.class, fault);
			}
		} catch (Exception e) {
			throw new Fault(e);
		}	
	}

	private static byte[] extractInputstream(InputStream content) throws IOException{
		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			int count = 0;
			byte [] buf = new byte[16384];
			while ((count = content.read(buf, 0, buf.length)) > 0) {
				baos.write(buf, 0, count);
			}
			return baos.toByteArray();
		}
	}
	
	protected abstract Logger getLogger();

}
