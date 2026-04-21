package com.example.demo.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import model.Product;

public interface ProductRepo extends JpaRepository<Product, Integer>{
	
	@Query("select p from Product p where p.typeBean.name like %?1%")
	List<Product> findByType(String type);

}
