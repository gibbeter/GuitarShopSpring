package com.example.demo.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import model.Overview;
import model.OverviewPK;
import model.Product;
import model.User;

public interface OverviewRepo extends JpaRepository<Overview, OverviewPK>{
	
	@Query("select o from Overview o where o.id.productId =?1")
	List<Overview> findByProduct(Integer prodId);

	@Modifying
	@Query("delete from Overview where id.productId =?1")
	void deleteAllByProductId(int prodId);
	
}
