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
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

@RestController
public class YPServiceController {
	Logger logger = LoggerFactory.getLogger(YPServiceController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private EurekaClient eurekaClient;

	@RequestMapping(value = "/yourprocure", method = RequestMethod.POST)
	public String processTransaction(@RequestBody String xml) {
		
		logger.info("Starting Your procure Transaction ");
		
		InstanceInfo instanceInfo = null;
		String url = null;
		String response = null;
		Application aribaApplication = null;
		
		
		//perform transaction transform and update in Ariba service 
		aribaApplication = eurekaClient.getApplication("sapariba-service");
		instanceInfo = aribaApplication.getInstances().get(0);
		url = "http://" + instanceInfo.getIPAddr() + ":"+ instanceInfo.getPort() + "/" + "/ariba/";
		
		response = restTemplate.postForObject(url, transformRecievedData(xml), String.class);

		logger.info("Finishing your procure transaction ");
		
		return response.toString();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
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
