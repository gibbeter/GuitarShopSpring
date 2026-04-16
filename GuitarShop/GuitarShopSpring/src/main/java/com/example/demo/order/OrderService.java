package com.example.demo.order;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.GuitarShopSpringApplication;
import com.example.demo.product.ProductDTO;
import com.example.demo.product.ProductRepo;

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
			System.out.println("UserID: " +  o.getUserId());
			o = orderRepo.save(o);
			return orderToDTO(o);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
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
			return orderToDTO(o);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	public List<OrderDTO> findAllOrders() {
		try {
			List<Order> list = orderRepo.findAll();
			List<OrderDTO> res = new ArrayList<>();
			for(Order o: list) {
				res.add(orderToDTO(o));
			}
			return res;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}		
	}

	public OrderDTO findOrder(Integer orderId) {
		try {
			Order o = orderRepo.findById(orderId).get();
			return orderToDTO(o);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public boolean modifyOrder(OrderDTO order) {
		try {
			Order o = orderRepo.findById(order.getOrderId()).get();
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
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Transactional
	public boolean deleteOrder(OrderDTO order) {
		try {
			Order o = orderRepo.findById(order.getOrderId()).get();
			for(Orderitem i : o.getOrderitems()) {
				orderItemRepo.delete(i);
			}
			orderRepo.delete(o);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
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
		}catch(Exception e) {
			e.printStackTrace();
			return null;
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
		}catch(Exception e) {
			e.printStackTrace();
			return null;
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
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}	
	}

}
   