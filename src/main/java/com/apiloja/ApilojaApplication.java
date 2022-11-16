package com.apiloja;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


//http://localhost:8080/swagger-ui/index.html

//Authorization: Bearer "token aqui"

@ComponentScan({
		"com.apiloja.controller",
		"com.apiloja.security",
		"com.apiloja.service"

})
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ApilojaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApilojaApplication.class, args);


	}

}
