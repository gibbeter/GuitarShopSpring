package com.example.demo.order;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import model.Order;
import model.OrderitemPK;


@Validated
public class OrderItemDTO {

	@NotNull
	private OrderitemPK id;

	@NotNull
	private Integer price;

	@NotNull
	private Integer quantity;

	@NotNull
	private Order order;

	public OrderitemPK getId() {
		return id;
	}

	public void setId(OrderitemPK id) {
		this.id = id;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public OrderItemDTO(@NotNull OrderitemPK id, @NotNull Integer price, @NotNull Integer quantity,
			@NotNull Order order) {
		this.id = id;
		this.price = price;
		this.quantity = quantity;
		this.order = order;
	}
	
	public OrderItemDTO(@NotNull OrderitemPK id, @NotNull Integer price, @NotNull Integer quantity) {
		this.id = id;
		this.price = price;
		this.quantity = quantity;
	}
	
	public OrderItemDTO() {

	}

	@Override
	public String toString() {
		return "OrderItemDTO [id=" + id + ", price=" + price + ", quantity=" + quantity + ", order=" + order + "]";
	}
	
	
}
