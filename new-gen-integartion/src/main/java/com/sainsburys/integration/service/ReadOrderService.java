package com.sainsburys.integration.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sainsburys.integration.data.OrderDAOImpl;
import com.sainsburys.integration.models.Order;
import com.sainsburys.integration.utility.conversion.StringToSqlDateConverter;
@Service
public class ReadOrderService {

	@Autowired
	public OrderDAOImpl orderDAOImpl;
	
	public final String dateFormat="dd/mm/yyyy";
	public List<Order> processFilesFromFileServer(String filepath) {
		String csvFile = "/Users/dharma.mittapalli/Documents/integration/new-gen-integration/new-gen-integartion/orders/orders.txt";
		String line = "";
		String cvsSplitBy = ",";
		List<Order> orders = new ArrayList<Order>();
		System.out.println("filepath"+filepath);
		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			
			while ((line = br.readLine()) != null) {
				System.out.println("record: "+line);
				Order order = new Order();
				String[] order_info = line.split(cvsSplitBy);
			    order.setOrderId(Integer.parseInt(order_info[0]));
			    order.setQuantity(Integer.parseInt(order_info[1]));
			    order.setShipmentDate(new StringToSqlDateConverter(dateFormat).convert(order_info[2]));
			    //order.setShipmentDate(order_info[2]);
			    order.setVehicleId(Integer.parseInt(order_info[3]));
			    order.setSupplierId(order_info[4]);
			    order.setSupplierName(order_info[5]);
			    order.setProductId(order_info[6]);
			    order.setProductName(order_info[7]);
			    order.setDepotId(order_info[8]);
			    order.setChannel(order_info[9]);
			    System.out.println("order: "+order);
			    orders.add(order);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return orders;
	}

	public List<Order> getOrders() {
		
		return orderDAOImpl.getOrders();
	}
	
}
