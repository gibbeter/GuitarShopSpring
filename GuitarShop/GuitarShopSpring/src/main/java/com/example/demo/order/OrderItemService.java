package com.example.demo.order;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.product.ProductDTO;
import com.example.demo.product.ProductRepo;

import jakarta.validation.ValidationException;
import model.Cart;
import model.Cartitem;
import model.CartitemPK;
import model.Orderitem;
import model.Product;
import model.Type;

@Service
public class OrderItemService {
	
	@Autowired
	OrderItemRepo orderItemRepo;
	
	@Autowired
	OrderRepo orderRepo;
	
	@Autowired
	ProductRepo prodRepo;

	public OrderItemDTO saveItem(OrderItemDTO orderItemDTO) {
		try {
			Orderitem i = new Orderitem();
			i.setId(orderItemDTO.getId());
			i.setPrice(orderItemDTO.getPrice());
			i.setQuantity(orderItemDTO.getQuantity());
			i = orderItemRepo.save(i);
			return itemToDTO(i);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public OrderItemDTO itemToDTO(Orderitem o) {
		return new OrderItemDTO(o.getId(), o.getPrice(), o.getQuantity());
	}

	
}
