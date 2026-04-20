package com.example.demo.order;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.GuitarShopSpringApplication;
import com.example.demo.cartitem.CartController;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.InvalidBackUpPathException;
import com.example.demo.files.BackUpService;
import com.example.demo.product.ProductDTO;
import com.example.demo.product.ProductRepo;
import com.example.demo.user.UserService;

import jakarta.validation.ValidationException;
import model.Cart;
import model.Cartitem;
import model.CartitemPK;
import model.Order;
import model.Orderitem;
import model.Product;
import model.Type;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class OrderService {
	
	@Autowired
	OrderRepo orderRepo;
	
	@Autowired
	OrderItemRepo orderItemRepo;
	
	@Autowired
	BackUpService backupService;
	
	private static final Logger log = LoggerFactory.getLogger(OrderService.class);


	public OrderDTO createOrder(OrderDTO order) {
		try {
			Order o = new Order();
//			System.out.println("UserID: " +  order.getUserId());
			o.setUserId(order.getUserId());
			o.setOrderDate(order.getOrderDate());
			o.setOrderStatus(order.getOrderStatus());
//			if (o.getUserId() == 0) {
//			    // либо создать гостя, либо выбросить ошибку
//			    throw new IllegalArgumentException("User ID must not be null");
//			}
//			o.setUserId(1);
//			System.out.println("UserID: " +  o.getUserId());
			o = orderRepo.save(o);
			return orderToDTO(o);
		}catch(DataAccessException e) {
		    log.error("Database error: {}", e.getMessage());
		    throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}
	
	public OrderDTO orderToDTO(Order o) {
		return new OrderDTO(o.getOrderId(), o.getOrderDate(), o.getOrderStatus(), o.getUserId(), o.getName(), o.getSurname(), o.getPhoneNumber(), o.getSumm(), o.getOrderType(), o.getPickupAdress(),
				o.getShippingAdress(), o.getCompletionTime(), itemsToDTO(o.getOrderitems()));
	}
	
//	public Order orderFromDTO(OrderDTO o) {
//		return new Order(o.getOrderId(), o.getOrderDate(), o.getOrderStatus(), o.getUserId(), itemsFromDTO(o.getOrderitems()));
//	}
	
	public List<OrderItemDTO> itemsToDTO(List<Orderitem> list) {
		List<OrderItemDTO> res = new ArrayList<>();
		if(list != null) {
			for(Orderitem i : list) {
				res.add(new OrderItemDTO(i.getId(), i.getPrice(), i.getQuantity()));
			}
			return res;
		}
		return null;
	}
	
	public List<Orderitem> itemsFromDTO(List<OrderItemDTO> list) {
		List<Orderitem> res = new ArrayList<>();
		if(list != null) {
			for(OrderItemDTO i : list) {
				res.add(new Orderitem(i.getId(), i.getPrice(), i.getQuantity()));
			}
			return res;
		}
		return null;
	}

	public OrderDTO saveOrder(OrderDTO order) {
		try {
			Order o = new Order();
			o.setOrderId(order.getOrderId());
			o.setOrderDate(order.getOrderDate());
			o.setOrderStatus(order.getOrderStatus());
			o.setUserId(order.getUserId());
			o.setName(order.getName());
			o.setSurname(order.getSurname());
			o.setPhoneNumber(order.getPhoneNumber());
			o.setSumm(order.getSumm());
			o.setOrderitems(itemsFromDTO(order.getOrderitems()));
			o.setOrderType(order.getOrderType());
			o.setPickupAdress(order.getPickupAdress());
			o.setShippingAdress(order.getShippingAdress());
			o.setCompletionTime(order.getCompletionTime());
			o = orderRepo.save(o);
			

			saveToCSV();
			return orderToDTO(o);

		}catch(DataAccessException e) {
		    log.error("Database error: {}", e.getMessage());
		    throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}
	
	

	public List<OrderDTO> findAllOrders() {
		try {
			List<Order> list = orderRepo.findAll();
			List<OrderDTO> res = new ArrayList<>();
			for(Order o: list) {
				res.add(orderToDTO(o));
			}
			return res;
		}catch(DataAccessException e) {
		    log.error("Database error: {}", e.getMessage());
		    throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}	
	}

	public OrderDTO findOrder(Integer orderId) {
		try {
			Optional<Order> existing = orderRepo.findById(orderId);
			
			if(existing.isEmpty()) {
				log.warn("Order not found with id: {}", orderId);
				throw new EntityNotFoundException("Order", orderId);
			}
			
			return orderToDTO(existing.get());
		}catch(DataAccessException e) {
		    log.error("Database error: {}", e.getMessage());
		    throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	@Transactional
	public boolean modifyOrder(OrderDTO order) {
		try {
			Integer orderId = order.getOrderId();
			Optional<Order> existing = orderRepo.findById(orderId);
			
			if(existing.isEmpty()) {
				log.warn("Order not found with id: {}", orderId);
				throw new EntityNotFoundException("Order", orderId);
			}
			
			Order o = existing.get();
			o.setName(order.getName());
			o.setSurname(order.getSurname());
			o.setPhoneNumber(order.getPhoneNumber());
			o.setOrderStatus(order.getOrderStatus());
			o.setSumm(order.getSumm());
			o.setOrderType(order.getOrderType());
			o.setPickupAdress(order.getPickupAdress());
			o.setShippingAdress(order.getShippingAdress());
			o.setCompletionTime(order.getCompletionTime());
			o = orderRepo.save(o);


			saveToCSV();
			return true;

		}catch(DataAccessException e) {
		    log.error("Database error: {}", e.getMessage());
		    throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	@Transactional
	public boolean deleteOrder(OrderDTO order) {
		try {
			Integer orderId = order.getOrderId();
			Optional<Order> existing = orderRepo.findById(orderId);
			
			if(existing.isEmpty()) {
				log.warn("Order not found with id: {}", orderId);
				throw new EntityNotFoundException("Order", orderId);
			}
			
			Order o = existing.get();
			
			for(Orderitem i : o.getOrderitems()) {
				orderItemRepo.delete(i);
			}
			orderRepo.delete(o);

			saveToCSV();
			return true;
			
		}catch(DataAccessException e) {
		    log.error("Database error: {}", e.getMessage());
		    throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	public List<OrderDTO> findOrderByStatus(String status) {
		try {
			List<Order> list = orderRepo.findAllByStatus(status);
			List<OrderDTO> res = new ArrayList<>();
			for(Order o: list) {
				res.add(orderToDTO(o));
			}
			return res;
		}catch(DataAccessException e) {
		    log.error("Database error: {}", e.getMessage());
		    throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	public List<OrderDTO> findAllUserOrders(Integer userId) {
		try {
			List<Order> list = orderRepo.findAllByUserId(userId);
			List<OrderDTO> res = new ArrayList<>();
			for(Order o: list) {
				res.add(orderToDTO(o));
			}
			return res;
		}catch(DataAccessException e) {
		    log.error("Database error: {}", e.getMessage());
		    throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	public Object findUserOrderByStatus(Integer userId, String status) {
		try {
			List<Order> list = orderRepo.findAllByUserIdAndOrderStatus(userId, status);
			List<OrderDTO> res = new ArrayList<>();
			for(Order o: list) {
				res.add(orderToDTO(o));
			}
			return res;
		}catch(DataAccessException e) {
		    log.error("Database error: {}", e.getMessage());
		    throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}
	
	private void saveToCSV(){

        try{
        	List<OrderDTO> ordersList = ordersToDTO(orderRepo.findAll());
    		String prefix = "orders";
    		String fileName = "orders";
            String[] header = {"order_id","order_status","order_date","user_id","order_type",
            					"pu_adress","sp_adress","comp_time","summ","name","surname","phone_number"};
            
            Function<OrderDTO, String[]> orderMapper = order -> new String[]{
            		safeString(order.getOrderId()),
            	    safeString(order.getOrderStatus()),
            	    safeString(order.getOrderDate()),
            	    safeString(order.getUserId()),
            	    safeString(order.getOrderType()),
            	    safeString(order.getPickupAdress()),
            	    safeString(order.getShippingAdress()),
            	    safeString(order.getCompletionTime()),
            	    safeString(order.getSumm()),
            	    safeString(order.getName()),
            	    safeString(order.getSurname()),
            	    safeString(order.getPhoneNumber())
            	};
            backupService.saveBackup(ordersList, prefix, fileName, header, orderMapper);
        } catch(InvalidBackUpPathException e) {
			log.error("Backup error: {}", e.getMessage());
		}
		
	}
	
	private String safeString(Object obj) {
	    return obj != null ? obj.toString() : "";
	}

	private List<OrderDTO> ordersToDTO(List<Order> list) {
		List<OrderDTO> res = new ArrayList<>();
		if(list != null) {
			for(Order o : list) {
				res.add(orderToDTO(o));
			}
			return res;
		}
		return null;
	}

}
   