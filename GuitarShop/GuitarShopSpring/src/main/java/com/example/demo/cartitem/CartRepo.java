package com.example.demo.cartitem;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import model.Cart;

public interface CartRepo extends JpaRepository<Cart, Integer>{
	
	@Query("select c from Cart c where c.userId =?1")
	Optional<Cart> findByUser(Integer id);

}
