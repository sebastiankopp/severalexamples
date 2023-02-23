package de.sebastiankopp.severalexamples.bootysoap.batch.control;

import java.nio.file.Paths;
import java.util.Arrays;

import javax.sql.DataSource;

import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import de.sebastiankopp.severalexamples.bootysoap.batch.entity.Person;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
// Skip executing batch job on startup by adding sysprop <code>spring.batch.job.enabled=false</code> or add it to application.properties
public class BatchConf {
	@Autowired
	DataSource dataSource;
	
	@Autowired
	Logger logger;
	
	@Autowired
	PersonProcessor processor;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Autowired
	JobRepository jobRepository;
	
	FlatFileItemReader<Person> reader() {
		FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(Paths.get("testdata", "sample.csv")));
//		reader.setLinesToSkip(linesToSkip);
		reader.setLineMapper((lineContent, lineNumber) -> {
			String[] split = lineContent.split("[;,]");
			if (split.length == 2 && Arrays.stream(split).noneMatch(e -> e == null || e.isEmpty())) {
				Person person = new Person(split[0], split[1]);
				logger.debug("Person successfully read: {}", person);
				return person;
			} else {
				logger.warn("Invalid line: {}. Result will be null.", lineContent);
				return null;
			}
		});
		return reader;
	}
	
	JdbcBatchItemWriter<Person> writer() {
		JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.setSql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName);");
		writer.setItemPreparedStatementSetter((pers, prepStat) -> {
			prepStat.setString(1, pers.getFirstName());
			prepStat.setString(2, pers.getLastName());
		});
		writer.setDataSource(dataSource);
		return writer;
	}
	
	@Bean
	public Job importUserJob(ExampleCompletionListener completionListener) {
		return new JobBuilder("importUserJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.listener(completionListener)
				.flow(step1())
				.end()
				.build();
	}
	
	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
				.allowStartIfComplete(true)
				.<Person,Person>chunk(10)
				.reader(reader())
				.processor(processor)
				.writer(writer())
				.transactionManager(transactionManager)
				.build();
	}
}
