package de.sebastiankopp.severalexamples.bootysoap.batch.control;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Path("batch-trigger")
@Component
public class BatchTriggeringController {

	@Autowired
	JobLauncher launcher;
	
	@Autowired
	Job importUserJob;
	
	@GET
	public void exec() throws Exception {
		launcher.run(importUserJob, new JobParameters());
	}
	
}
