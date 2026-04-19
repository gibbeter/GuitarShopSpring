package com.example.demo.adress;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Validated
public class StoreAdressDTO {
	
	@NotNull
	private Integer storeId;

	@Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ'’\\-\\.\\s]+,\\s*[A-Za-zÀ-ÖØ-öø-ÿ'’\\-\\.\\s]+,\\s*[A-Za-z0-9\\-]+,\\s*[A-Za-z0-9\\-]+$",
            message = "Format must be: CITY, STREET, NUMBER, APARTMENT")
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
