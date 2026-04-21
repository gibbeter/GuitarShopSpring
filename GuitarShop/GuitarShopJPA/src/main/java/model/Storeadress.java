package model;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the storeadress database table.
 * 
 */
@Entity
@Table(name="StoreAdress")
@NamedQuery(name="Storeadress.findAll", query="SELECT s FROM Storeadress s")
public class Storeadress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="store_id")
	private int storeId;

	@Column(name="store_adress")
	private String storeAdress;

	public Storeadress() {
	}

	public int getStoreId() {
		return this.storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getStoreAdress() {
		return this.storeAdress;
	}

	public void setStoreAdress(String storeAdress) {
		this.storeAdress = storeAdress;
	}

}