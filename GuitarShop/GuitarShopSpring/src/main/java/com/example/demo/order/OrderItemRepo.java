package com.example.demo.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import model.Cartitem;
import model.CartitemPK;
import model.Orderitem;
import model.OrderitemPK;

public interface OrderItemRepo extends JpaRepository<Orderitem, OrderitemPK>{

	@Query("select ci from Cart c join c.cartitems ci where ci.id =?1")
	Cartitem findItemInCart(CartitemPK id);
	
	@Query("select ci from Cart c join c.cartitems ci where c.userId =?1")
	List<Cartitem> findItemsByUser(Integer userId);
	
	@Modifying
	@Query("delete from Cartitem where product.id =?1")
	void deleteAllByProductId(int prodId);

}
