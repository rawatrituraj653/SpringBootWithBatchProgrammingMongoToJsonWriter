package com.st.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;
import com.st.model.Product;

@Configuration
@EnableBatchProcessing
public class MongoBatchConfig {
	@Autowired
	private JobBuilderFactory jf;
	@Autowired
	private StepBuilderFactory sf;
	
	@Bean
	public Job doJob() {
		
		return jf.get("job")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.start(dowork())
				.build();
	}
	
	@Bean
	public Step dowork() {
		
		return sf.get("step")
				.<Product,Product>chunk(4)
				.reader(reader())
				.processor(process())
				.writer(write())
				.build();
	}	
	
	
	@Bean
	public ItemReader<Product> reader(){
		Map<String, Sort.Direction> map=new HashMap<>();
		map.put("pid", Direction.ASC);
		MongoItemReader<Product> read=new MongoItemReader<>();
		read.setTemplate(new MongoTemplate(new MongoClient("localhost", 27017),"mylib"));
		read.setCollection("product");
		read.setQuery("{}");
		read.setSort(map);
		read.setTargetType(Product.class);
		return read;
	}
	@Bean
	public ItemProcessor<Product,Product> process(){

		return item->item;
	}
	@Bean
	public ItemWriter<Product>  write(){
		
		return  new JsonFileItemWriterBuilder<Product>()
				.jsonObjectMarshaller(new JacksonJsonObjectMarshaller<Product>())
				.resource(new ClassPathResource("product.json"))
				.name("productjson")
				.build();
	}
	
	@Bean
	public JobExecutionListener  listener() {
		
		return new JobExecutionListener() {
			
			@Override
			public void beforeJob(JobExecution jobExecution) {
			System.out.println("Before Job: "+jobExecution.getStatus().toString());	
			}
			
			@Override
			public void afterJob(JobExecution jobExecution) {
				System.out.println("After Job: "+jobExecution.getStatus().toString());	
			}
		};
	} 
		
}
