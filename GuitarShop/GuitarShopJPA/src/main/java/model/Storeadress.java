package model;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the storeadress database table.
 * 
 */
@Entity
@NamedQuery(name="Storeadress.findAll", query="SELECT s FROM Storeadress s")
public class Storeadress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="store_id")
	private int storeId;

	@Column(name="stroe_adress")
	private String stroeAdress;

	public Storeadress() {
	}

	public int getStoreId() {
		return this.storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getStroeAdress() {
		return this.stroeAdress;
	}

	public void setStroeAdress(String stroeAdress) {
		this.stroeAdress = stroeAdress;
	}

}