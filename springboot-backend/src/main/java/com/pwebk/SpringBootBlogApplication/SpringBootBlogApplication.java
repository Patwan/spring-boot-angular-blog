package com.pwebk.SpringBootBlogApplication;

import com.pwebk.SpringBootBlogApplication.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class SpringRedditCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(com.programming.techie.springredditclone.SpringRedditCloneApplication.class, args);
	}

}