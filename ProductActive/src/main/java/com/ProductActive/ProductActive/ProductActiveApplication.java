package com.ProductActive.ProductActive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class
ProductActiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductActiveApplication.class, args);
	}

}
