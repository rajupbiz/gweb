package com.blob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.blob"})
public class WebdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebdemoApplication.class, args);
	}
}
