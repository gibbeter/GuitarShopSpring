package com.example.demo.cartitem;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import model.Cart;
import model.Product;
import model.User;

public interface CartRepo extends JpaRepository<Cart, Integer>{
	
	@Query("select c from Cart c where c.userId =?1")
	Optional<Cart> findByUser(Integer id);

}
