package com.ibm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ibm.bean.RequisitionDTO;
import com.ibm.consants.YPServiceConstants;
import com.ibm.input.handler.TransformInput;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class YPServiceController {
	Logger logger = LoggerFactory.getLogger(YPServiceController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private EurekaClient eurekaClient;
	
	@HystrixCommand(fallbackMethod="reliable", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"), //default value , number of request which will trip the circuit
			@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="25000"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "25000") })

	@RequestMapping(value = "/yourprocure", method = RequestMethod.POST)
	public String processTransaction(@RequestBody String xml) {
		
		logger.info("Starting Your procure Transaction ");
		
		InstanceInfo instanceInfo = null;
		String url = null;
		String response = null;
		Application aribaApplication = null;
		TransformInput transformInput = null;
		
		transformInput = new TransformInput();
		
		//perform transaction transform and update in Ariba  service 
		aribaApplication = eurekaClient.getApplication("sapariba-service");
		instanceInfo = aribaApplication.getInstances().get(0);
		url = "http://" + instanceInfo.getIPAddr() + ":"+ instanceInfo.getPort() + "/" + "/ariba/";
		
		response = restTemplate.postForObject(url, transformInput.transformInput(xml), String.class);

		logger.info("Finishing your procure transaction ");
		
		return response.toString();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	
	public String reliable(@RequestBody String recievedData ) {
		
		logger.info(" Falling back for YP Transaction ");
		
		String response = "<returnData><error>Circuit broke at Ariba Service for YP transaction :"+new TransformInput().transformInput(recievedData).getApplicationTransactionNumber()+"</error></returnData>";


		logger.info("Finishing Fall back YP  transaction ");
		
		return response.toString();
	}
	
	/**
	 * @param xml
	 * @return
	 */
	private RequisitionDTO transformRecievedData(String xml){
		RequisitionDTO requisitionDTO = null;
		
		requisitionDTO = new RequisitionDTO();
		requisitionDTO.setApplicationType(YPServiceConstants.APP_TYPE);
		
		//TODO: complete the transformation of the recieved data
		
		return requisitionDTO;
	}
}
