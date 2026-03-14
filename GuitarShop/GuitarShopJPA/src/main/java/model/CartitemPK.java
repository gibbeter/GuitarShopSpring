package model;

import java.io.Serializable;
import jakarta.persistence.*;

/**
 * The primary key class for the cartitem database table.
 * 
 */
@Embeddable
public class CartitemPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="cart_id")
	private int cartId;

	@Column(name="prod_id")
	private int prodId;

	
	
	public CartitemPK(int cartId, int prodId) {
		this.cartId = cartId;
		this.prodId = prodId;
	}
	
	public CartitemPK() {
	}
	public int getCartId() {
		return this.cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
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
		if (!(other instanceof CartitemPK)) {
			return false;
		}
		CartitemPK castOther = (CartitemPK)other;
		return 
			(this.cartId == castOther.cartId)
			&& (this.prodId == castOther.prodId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.cartId;
		hash = hash * prime + this.prodId;
		
		return hash;
	}
}