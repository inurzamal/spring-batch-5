package com.springbatch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

    public static final Logger log = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    @Qualifier("myJob")
    private Job firstJob;

    @Autowired
    @Qualifier("anotherJob")
    private Job secondJob;

    @GetMapping("/start/{jobName}")
    public void startJob(@PathVariable String jobName) throws Exception{
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();

        if (jobName.equalsIgnoreCase("myJob")) {
            jobLauncher.run(firstJob, jobParameters);
        } else if (jobName.equalsIgnoreCase("anotherJob")) {
            jobLauncher.run(secondJob, jobParameters);
        }
        else {
            log.error("Error while starting Job at JobController");
        }

    }
}
