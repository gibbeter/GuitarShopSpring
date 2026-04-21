package com.example.demo.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import model.Order;

public interface OrderRepo extends JpaRepository<Order, Integer>{
	
//	@Query("select c from Cart c where c.userId =?1")
//	Cart findByUser(Integer id);

	@Query("select o from Order o where o.orderStatus =?1")
	List<Order> findAllByStatus(String status);

	List<Order> findAllByUserId(Integer userId);

	List<Order> findAllByUserIdAndOrderStatus(Integer userId, String status);

}
