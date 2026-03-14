package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("model")
public class GuitarShopSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuitarShopSpringApplication.class, args);
	}

}
