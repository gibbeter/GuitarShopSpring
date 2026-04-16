package com.example.demo.adress;

import jakarta.persistence.Column;

public class StoreAdressDTO {
	
	private Integer storeId;

	private String storeAdress;

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreAdress() {
		return storeAdress;
	}

	public void setStoreAdress(String storeAdress) {
		this.storeAdress = storeAdress;
	}

	public StoreAdressDTO(Integer storeId, String storeAdress) {
		super();
		this.storeId = storeId;
		this.storeAdress = storeAdress;
	}
	
	
}
