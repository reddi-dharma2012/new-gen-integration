package com.sainsburys.integration.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sainsburys.integration.adaptor.ReadOrderService;
import com.sainsburys.integration.data.AdviceMessageDao;
import com.sainsburys.integration.facade.PublisherService;
import com.sainsburys.integration.models.AdviceMessage;
import com.sainsburys.integration.models.Order;


@RestController
public class IntegrationController {
	 private static final Logger LOG = LoggerFactory.getLogger(IntegrationController.class);
	@Autowired
	public PublisherService service;
	@Autowired
	public ReadOrderService readOrderService;
	
	@Autowired
	public AdviceMessageDao shipmentRepositoryService;
	
	
    @RequestMapping(value="/integration" ,method = RequestMethod.POST)
	public String postItemDetails(@RequestBody Order order) {
    	System.out.println("Inside controller");
    	service.postItemsToMessageBus(order);
    	return "OK-Pass";
    	
	}
    
    @RequestMapping(value="/processOrders" ,method = RequestMethod.GET)
   	public String processOrders() {
    	System.out.println("processOrders inside controller");
    	String csvFile = "/Users/dharma.mittapalli/Documents/integration/new-gen-integration/new-gen-integartion/orders/orders.txt";
		
    	List<Order> orders = readOrderService.processFilesFromFileServer(csvFile);
    	for(Order order :orders) {
    		service.postItemsToMessageBus(order);
    	}
       	return "OK";
   	}
    
    @RequestMapping(value="/getOrders" ,method = RequestMethod.GET)
   	public List<Order> getOrders() {
    	System.out.println("processOrders inside controller");
    	List<Order> orders =null;
    	orders =readOrderService.getOrders();
    	if (!orders.isEmpty())
    		return orders;
    	else
    		return null;
   	}
    
    @RequestMapping(value="/getShipments" ,method = RequestMethod.GET)
   	public Iterable<AdviceMessage> getShipments() {
    	CrudRepository<AdviceMessage, String> crudRepository = (CrudRepository<AdviceMessage, String>) shipmentRepositoryService;
		return crudRepository.findAll();
   	}
    
    @RequestMapping(value="/saveShipment" ,method = RequestMethod.GET)
   	public AdviceMessage saveShipments() {
    	AdviceMessage message = new AdviceMessage();
    	message.setSku("12345");
    	message.setShipmentNumber("658658");
    	CrudRepository<AdviceMessage, String> crudRepository = (CrudRepository<AdviceMessage, String>) shipmentRepositoryService;
		return crudRepository.save(message);
   	}
  
    @RequestMapping(value="/getShipments/{id}" ,method = RequestMethod.GET)
   	public Optional<AdviceMessage> getShipmentById(@PathVariable String id) {
    	CrudRepository<AdviceMessage, String> crudRepository = (CrudRepository<AdviceMessage, String>) shipmentRepositoryService;
		return crudRepository.findById(id);
   	}

  
    
}
