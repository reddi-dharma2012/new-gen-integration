package com.sainsburys.integration.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.sainsburys.integration.data.AdviceMessageDao;
import com.sainsburys.integration.data.OrderDAOImpl;
import com.sainsburys.integration.models.AdviceMessage;
import com.sainsburys.integration.models.Order;

@Service
public class ConsumerService {

	private static final Logger LOG = LoggerFactory.getLogger(ConsumerService.class);
	@Autowired
	public OrderDAOImpl orderDAOImpl;
	
	@Autowired
	public AdviceMessageDao shipmentRepositoryService;


//	@KafkaListener(topics = "test")
//	public void listen(@Payload Order order) {
//		System.out.println("received message='{}'" + order);
//		LOG.info("received message='{}'", order);
//		orderDAOImpl.create(order);
//	}
	
	@KafkaListener(topics = "test")
	public void listen(@Payload AdviceMessage supplierAdviseMessage) {
		System.out.println("received message='{}'" + supplierAdviseMessage);
		LOG.info("received message='{}'", supplierAdviseMessage);
		//orderDAOImpl.create(order);
		CrudRepository<AdviceMessage, String> crudRepository = (CrudRepository<AdviceMessage, String>) shipmentRepositoryService;
		AdviceMessage message =crudRepository.save(supplierAdviseMessage);
		LOG.info("No sql persisted message='{}'", message);
		
 
	}
}
