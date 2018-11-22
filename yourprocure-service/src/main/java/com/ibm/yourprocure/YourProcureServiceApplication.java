package com.ibm.yourprocure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = { "com.ibm.csaservice", "com.ibm.controller" })
public class YourProcureServiceApplication {

	public static void main(String[] args) {

		SpringApplication springApplication = new SpringApplication(YourProcureServiceApplication.class);
		springApplication.addListeners(new ApplicationPidFileWriter("ypservice.pid"));
		springApplication.run(args);
	}
}
