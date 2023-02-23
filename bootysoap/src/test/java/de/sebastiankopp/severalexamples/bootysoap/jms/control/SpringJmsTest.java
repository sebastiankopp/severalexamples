package de.sebastiankopp.severalexamples.bootysoap.jms.control;

import de.sebastiankopp.severalexamples.bootysoap.jms.entity.ReadyMsg;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SpringJmsTest {

	@Autowired
	JmsJunkie junkie;
	
	@Autowired
	JmsSender sender;
	
	@Test
	public void testMessaging() throws Exception {
		String msg = "Some foo " + randomUUID();
		sender.send(msg);
		TimeUnit.MILLISECONDS.sleep(200);
		ReadyMsg lastMsg = junkie.getLastMsg();
		assertNotNull(lastMsg);
		assertEquals(msg, lastMsg.getMsg());
	}
	
	
	
}
