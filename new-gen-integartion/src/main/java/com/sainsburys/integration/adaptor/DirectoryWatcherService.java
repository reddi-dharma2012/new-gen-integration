package com.sainsburys.integration.adaptor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;


import com.sainsburys.integration.models.Order;
import com.sainsburys.integration.adaptor.ReadOrderService;
import com.sainsburys.integration.facade.PublisherService;

@Component
public class DirectoryWatcherService implements InitializingBean{

	@Autowired
	public ReadOrderService readOrderService;
	@Autowired
	public PublisherService service;
	private WatchKey key;
	
	 @Override
	 public void afterPropertiesSet() throws Exception {
	        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(13);
	        scheduler.scheduleAtFixedRate(new Runnable() {
	            @Override
	            public void run() {
	                System.out.println("Hello from scheduler");  
	                pollFileServer();
	            }
		}, 0, 5, TimeUnit.SECONDS);
	    }

	public void pollFileServer() {
		String directoryPath = "/Users/dharma.mittapalli/Documents/fileserver";
		Path path = Paths.get(directoryPath);
		WatchService watchService = null;
		// Path path = Paths.get(System.getProperty("user.home"));
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while ((key = watchService.take()) != null) {
				for (WatchEvent<?> event : key.pollEvents()) {
					System.out.println("Event" + event.kind() + ". File affected: " + event.context() + ".");
				    System.out.println(StandardWatchEventKinds.ENTRY_CREATE);
					if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
						System.out.println("Event" + event.kind());
						List<Order> orders = readOrderService
								.processFilesFromFileServer(directoryPath + "/" + event.context());
						for (Order order : orders) {
							service.postItemsToMessageBus(order);
						}
					}
				}
				key.reset();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}