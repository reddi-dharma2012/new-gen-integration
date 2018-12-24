package com.sainsburys.integration.listner;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sainsburys.integration.models.Order;
import com.sainsburys.integration.service.MessageProducerService;
import com.sainsburys.integration.service.ReadOrderService;

@Service
public class DirectoryWatcherService implements InitializingBean, DisposableBean {

	@Autowired
	public ReadOrderService readOrderService;
	@Autowired
	public MessageProducerService service;
	private WatchKey key;

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
					if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
						System.out.println("Event" + event.kind() );
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

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Initializing the Directory Watch service");
		pollFileServer();
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("Killing  the Directory Watch service");
		if (key != null)
			key.cancel();

	}
}