package com.example.demo.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import model.Type;

public interface TypeRepo extends JpaRepository<Type, Integer>{

	@Query("select t from Type t where t.name =?1")
	Type findByName(String typeName);

}
