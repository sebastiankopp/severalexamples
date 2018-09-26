package de.sebikopp.bootysoap.jms.control;

import static java.util.UUID.randomUUID;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import de.sebikopp.bootysoap.jms.entity.ReadyMsg;

@SpringBootTest
public class SpringJmsTest extends AbstractTestNGSpringContextTests {

	@Autowired
	JmsJunkie junkie;
	
	@Autowired
	JmsSender sender;
	
	@Test
	public void testName() throws Exception {
		String msg = "Some foo " + randomUUID();
		sender.send(msg);
		TimeUnit.MILLISECONDS.sleep(200);
		ReadyMsg lastMsg = junkie.getLastMsg();
		assertNotNull(lastMsg);
		assertEquals(lastMsg.getMsg(), msg);
	}
	
	
	
}
