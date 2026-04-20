package model;

import java.io.Serializable;
import jakarta.persistence.*;

/**
 * The primary key class for the overview database table.
 * 
 */
@Embeddable
public class OverviewPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="user_id")
	private int userId;

	@Column(name="product_id")
	private int productId;
	
	public OverviewPK(int userId, int productId) {
		this.userId = userId;
		this.productId = productId;
	}
	public OverviewPK() {
	}
	public int getUserId() {
		return this.userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return this.productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof OverviewPK)) {
			return false;
		}
		OverviewPK castOther = (OverviewPK)other;
		return 
			(this.userId == castOther.userId)
			&& (this.productId == castOther.productId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId;
		hash = hash * prime + this.productId;
		
		return hash;
	}
	@Override
	public String toString() {
		return "OverviewPK [userId=" + userId + ", productId=" + productId + "]";
	}
	
	
}