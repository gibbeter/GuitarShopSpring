package model;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the orderitem database table.
 * 
 */
@Entity
@Table(name="OrderItem")
@NamedQuery(name="Orderitem.findAll", query="SELECT o FROM Orderitem o")
public class Orderitem implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private OrderitemPK id;

	private int price;

	private int quantity;

	//bi-directional many-to-one association to Order
	@ManyToOne
	@JoinColumn(name="order_id", insertable=false, updatable=false)
	private Order order;

	public Orderitem() {
	}

	public OrderitemPK getId() {
		return this.id;
	}

	public void setId(OrderitemPK id) {
		this.id = id;
	}

	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Orderitem(OrderitemPK id, int price, int quantity) {
		this.id = id;
		this.price = price;
		this.quantity = quantity;
	}
	
}