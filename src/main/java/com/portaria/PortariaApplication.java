package com.portaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.portaria.*","com.infra.repositorio.*"} )
@EntityScan(basePackages = {"com.portaria.*","com.infra.*"} )
public class PortariaApplication extends SpringBootServletInitializer {
	
	 @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PortariaApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(PortariaApplication.class, args);
	}

}
