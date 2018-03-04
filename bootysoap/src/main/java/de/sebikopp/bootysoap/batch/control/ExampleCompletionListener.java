package de.sebikopp.bootysoap.batch.control;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import de.sebikopp.bootysoap.batch.entity.Person;

@Component
public class ExampleCompletionListener extends JobExecutionListenerSupport {
	
	private final Logger logger;
	
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public ExampleCompletionListener(Logger logger, JdbcTemplate jdbcTemplate) {
		this.logger = logger;
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			List<Person> resList = jdbcTemplate.query("select first_name, last_name from people",
					(rs, rowNum) -> new Person(rs.getString(1), rs.getString(2)));
			resList.forEach(e -> logger.info("Found {} in the database", e));
		}
	}
}
