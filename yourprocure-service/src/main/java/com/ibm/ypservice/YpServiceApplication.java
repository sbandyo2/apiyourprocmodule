package com.ibm.ypservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableHystrixDashboard
@ComponentScan(basePackages = { "com.ibm.ypservice","com.ibm.controller"} )

public class YpServiceApplication {
	
	public static void main(String[] args) {

		SpringApplication springApplication = new SpringApplication(YpServiceApplication.class);
		springApplication.addListeners(new ApplicationPidFileWriter("ypservice.pid"));
		springApplication.run(args);
	}

}
