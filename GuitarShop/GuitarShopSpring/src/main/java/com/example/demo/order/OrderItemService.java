package com.example.demo.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.exception.BusinessException;
import com.example.demo.product.ProductRepo;

import model.Orderitem;

@Service
public class OrderItemService {
	
	@Autowired
	OrderItemRepo orderItemRepo;
	
	@Autowired
	OrderRepo orderRepo;
	
	@Autowired
	ProductRepo prodRepo;
	
	private static final Logger log = LoggerFactory.getLogger(OrderItemService.class);

	public OrderItemDTO saveItem(OrderItemDTO orderItemDTO) {
		try {
			Orderitem i = new Orderitem();
			i.setId(orderItemDTO.getId());
			i.setPrice(orderItemDTO.getPrice());
			i.setQuantity(orderItemDTO.getQuantity());
			i = orderItemRepo.save(i);
			return itemToDTO(i);
		}catch(DataAccessException e) {
		    log.error("Database error: {}", e.getMessage());
		    throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}
	
	public OrderItemDTO itemToDTO(Orderitem o) {
		return new OrderItemDTO(o.getId(), o.getPrice(), o.getQuantity());
	}

	
}
