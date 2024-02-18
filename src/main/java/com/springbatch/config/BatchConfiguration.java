package com.springbatch.config;

import com.springbatch.service.FirstTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springbatch.listener.MyJobListener;
import com.springbatch.listener.MyStepListener;
import com.springbatch.processor.FirstItemProcessor;
import com.springbatch.reader.FirstItemReader;
import com.springbatch.service.SecondTasklet;
import com.springbatch.writer.FirstItemWriter;

@Configuration
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private FirstTasklet firstTasklet;

    @Autowired
    private SecondTasklet secondTasklet;

    @Autowired
    private MyJobListener myJobListener;

    @Autowired
    private MyStepListener myStepListener;

    @Autowired
    private FirstItemReader firstItemReader;

    @Autowired
    private FirstItemProcessor firstItemProcessor;

    @Autowired
    private FirstItemWriter firstItemWriter;

    private Step firstStep() {
        return stepBuilderFactory.get("First Step")
                .tasklet(firstTasklet)
                .listener(myStepListener)
                .build();
    }

    private Step secondStep() {
        return stepBuilderFactory.get("Second Step")
                .tasklet(secondTasklet)
                .build();
    }


    private Step firstChunkStep() {
        return stepBuilderFactory.get("First Chunk Step")
                .<Integer, Long>chunk(4)
                .reader(firstItemReader)
                .processor(firstItemProcessor)
                .writer(firstItemWriter)
                .build();
    }

	@Bean
	public Job myJob() {
		return jobBuilderFactory.get("my job")
				.incrementer(new RunIdIncrementer())
				.start(firstStep())
				.next(firstChunkStep())
				.next(secondStep())
				.listener(myJobListener)
				.build();
	}

//    @Bean
//    public Job anotherJob() {
//        return jobBuilderFactory.get("First Job")
//                .incrementer(new RunIdIncrementer())
//                .start(firstStep())
//                .next(secondStep())
//                .listener(myJobListener)
//                .build();
//    }

}
