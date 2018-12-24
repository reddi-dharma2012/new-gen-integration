package com.sainsburys.integration.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.sainsburys.integration.models.Order;


@Repository
public class OrderDAOImpl {

	//private final String INSERT_SQL = "INSERT INTO JS_Schema.order(order_id,quantity,shipment_date,vehicle_id,supplier_id,supplier_name,product_id,product_name,depot_id,channel) values(?,?,?,?,?,?,?,?,?,?)";
	  private final String INSERT_SQL = "INSERT INTO JS_Schema.order_details(order_id,quantity,shipment_date,vehicle_id,supplier_id,supplier_name,product_id,product_name,depot_id,channel) values(?,?,?,?,?,?,?,?,?,?)";
	  private final String SELECT_ORDERS= "Select * from JS_Schema.order_details";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Order> getOrders(){
		@SuppressWarnings("unchecked")
		List <Order>orders = jdbcTemplate.query(SELECT_ORDERS, new RowMapper(){
			 
		    public Order mapRow(ResultSet rs, int rowNum)
		            throws SQLException {
		        Order order = new Order(); 
		        order.setOrderId(rs.getInt("order_id"));
				order.setQuantity(rs.getInt("quantity"));
				order.setShipmentDate(rs.getDate("shipment_date"));
				order.setVehicleId(rs.getInt("vehicle_id"));
				order.setSupplierId(rs.getString("supplier_id"));
				order.setSupplierName(rs.getString("supplier_name"));
				order.setProductId(rs.getString("product_id"));
				order.setProductName(rs.getString("product_name"));
				order.setDepotId(rs.getString("depot_id"));
				order.setChannel(rs.getString("channel"));
		        return order;
		    }});
		return orders;
	}
	
	public Order create(final Order order) {
		System.out.println(INSERT_SQL);
		KeyHolder holder = new GeneratedKeyHolder();
		try {
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, order.getOrderId());
				ps.setInt(2, order.getQuantity());
				ps.setDate(3, order.getShipmentDate());
				//ps.setString(3, order.getShipmentDate());
				ps.setInt(4, order.getVehicleId());
				ps.setString(5, order.getSupplierId());
				ps.setString(6,order.getSupplierName());
				ps.setString(7, order.getProductId());
				ps.setString(8, order.getProductName());
				ps.setString(9, order.getDepotId());
				ps.setString(10, order.getChannel());
				return ps;
			}
		}, holder);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		//int newUserId = holder.getKey().intValue();
		return order;
	}



}


class OrderMapper implements RowMapper {

	@Override
	public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
		Order order = new Order();
		order.setOrderId(rs.getInt("order_id"));
		order.setQuantity(rs.getInt("quantity"));
		//order.setShipmentDate(rs.getDate("shipment_date"));
		order.setVehicleId(rs.getInt("vehicle_id"));
		order.setSupplierId(rs.getString("supplier_id"));
		order.setSupplierName(rs.getString("supplier_name"));
		order.setProductId(rs.getString("product_id"));
		order.setProductName(rs.getString("product_name"));
		order.setDepotId(rs.getString("depot_id"));
		order.setChannel(rs.getString("channel"));
	
		return order;
	}

}
