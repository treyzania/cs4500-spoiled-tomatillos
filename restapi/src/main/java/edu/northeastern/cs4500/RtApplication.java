package edu.northeastern.cs4500;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "edu.northeastern.cs4500")
public class RtApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
		return app.sources(RtApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(RtApplication.class, args);
	}

}
