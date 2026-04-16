package model;

import java.io.Serializable;
import jakarta.persistence.*;

/**
 * The primary key class for the orderitem database table.
 * 
 */
@Embeddable
public class OrderitemPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="order_id")
	private int orderId;

	@Column(name="prod_id")
	private int prodId;

	public OrderitemPK(Integer orderId, Integer prodId) {
		this.orderId = orderId;
		this.prodId = prodId;
	}
	
	public OrderitemPK() {
	}
	public int getOrderId() {
		return this.orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getProdId() {
		return this.prodId;
	}
	public void setProdId(int prodId) {
		this.prodId = prodId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof OrderitemPK)) {
			return false;
		}
		OrderitemPK castOther = (OrderitemPK)other;
		return 
			(this.orderId == castOther.orderId)
			&& (this.prodId == castOther.prodId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.orderId;
		hash = hash * prime + this.prodId;
		
		return hash;
	}
}