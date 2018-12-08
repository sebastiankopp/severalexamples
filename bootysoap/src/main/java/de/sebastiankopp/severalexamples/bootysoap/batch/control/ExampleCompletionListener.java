package de.sebastiankopp.severalexamples.bootysoap.batch.control;

import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.sebastiankopp.severalexamples.bootysoap.jms.control.JmsSender;

@Component
public class ExampleCompletionListener extends JobExecutionListenerSupport {
	
	private final Logger logger;
	
	private final JdbcPeopleReader rdr;

	private final JmsSender msgSender;
	
	@Autowired
	public ExampleCompletionListener(Logger logger, JdbcPeopleReader reader, JmsSender jmsSender) {
		this.logger = logger;
		rdr = reader;
		msgSender = jmsSender;
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			rdr.getAllPeople().forEach(e -> logger.info("Found {} in the database", e));
		}
		msgSender.send("Finished batch job!");
	}
}
