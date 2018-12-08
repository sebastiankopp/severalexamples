package de.sebastiankopp.severalexamples.bootysoap.jms.control;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import de.sebastiankopp.severalexamples.bootysoap.jms.entity.ReadyMsg;

@Component
public class JmsSender {

	static final String MYQUEUE = "myqueue";
	@Autowired
	JmsTemplate template;
	
	@Autowired
	Logger logger;
	
	public void send(String msg) {
		final ReadyMsg message = new ReadyMsg(msg);
		template.convertAndSend(MYQUEUE, message);
		logger.info("Sent a jms msg: {}", message);
	}
	
}
