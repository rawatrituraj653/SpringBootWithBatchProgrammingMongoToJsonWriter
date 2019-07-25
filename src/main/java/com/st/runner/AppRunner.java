package com.st.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

	@Autowired
	private Job job;
	@Autowired
	private JobLauncher launcher; 
	
	@Override
	public void run(ApplicationArguments args) throws Exception {

		System.out.println("Job Executions");
		launcher.run(job, new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters());
						
					

	}

}
