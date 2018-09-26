package de.sebikopp.bootysoap.jms.control;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import de.sebikopp.bootysoap.jms.entity.ReadyMsg;

@Component
public class JmsJunkie {
	
	@Autowired
	Logger logger;
	
	private final Deque<ReadyMsg> content = new ArrayDeque<>(new ArrayList<>());
	
	@JmsListener(destination=JmsSender.MYQUEUE, containerFactory=JmsConfig.CONTAINER_FACTORY)
	public void receiveJunk(ReadyMsg jnk) {
		content.addLast(jnk);
		logger.info("Redeived sth: {}", jnk);
		
	}
	
	public ReadyMsg getLastMsg() {
		return content.peekLast();
	}

}
