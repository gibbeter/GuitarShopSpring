package com.example.demo.cartitem;

import org.springframework.validation.annotation.Validated;

import com.example.demo.product.ProductDTO;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import model.Cart;
import model.CartitemPK;
import model.Product;


@Validated
public class ItemDTO {

	@NotNull
	private CartitemPK id;

	@NotNull
	@Min(0)
	private int quantity;

	//private CartDTO cart;

	@NotNull
	private ProductDTO product;
	
	public CartitemPK getId() {
		return id;
	}

	public void setId(CartitemPK id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

//	public CartDTO getCart() {
//		return cart;
//	}
//
//	public void setCart(CartDTO cart) {
//		this.cart = cart;
//	}

	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
	}

	public ItemDTO(CartitemPK id, int quantity, ProductDTO product) {
		this.id = id;
		this.quantity = quantity;
//		this.cart = cart;
		this.product = product;
	}

	public ItemDTO() {
		
	}

	@Override
	public String toString() {
		return "ItemDTO [id=" + id + ", quantity=" + quantity + ", product=" + product + "]";
	}
	
	
	
}
