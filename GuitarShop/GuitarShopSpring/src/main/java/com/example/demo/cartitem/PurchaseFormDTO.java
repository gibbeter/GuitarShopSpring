package com.example.demo.cartitem;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Validated
public class PurchaseFormDTO {
	
	@NotNull
	@NotBlank
	private String userName;
	@NotNull
	@NotBlank
	private String userSurname;
	@NotNull
	private Integer userPhone;
	@NotNull
	@NotBlank
	private String shippingType;
//	@NotNull
//	@NotBlank
//	@NotBlank(message = "Address is required"
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ'’\\-\\.\\s]+,\\s*[A-Za-zÀ-ÖØ-öø-ÿ'’\\-\\.\\s]+,\\s*[A-Za-z0-9\\-]+,\\s*[A-Za-z0-9\\-]+$",
             message = "Format must be: CITY, STREET, NUMBER, APARTMENT")
	private String SPAdress;
//	@NotNull
//	@NotBlank
	private String PUAdress;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserSurname() {
		return userSurname;
	}
	public void setUserSurname(String userSurname) {
		this.userSurname = userSurname;
	}
	public Integer getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(Integer userPhone) {
		this.userPhone = userPhone;
	}
	public String getShippingType() {
		return shippingType;
	}
	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}
	public String getSPAdress() {
		return SPAdress;
	}
	public void setSPAdress(String sPAdress) {
		SPAdress = sPAdress;
	}
	public String getPUAdress() {
		return PUAdress;
	}
	public void setPUAdress(String pUAdress) {
		PUAdress = pUAdress;
	}
	@Override
	public String toString() {
		return "PurchaseFormDTO [userName=" + userName + ", userSurname=" + userSurname + ", userPhone=" + userPhone
				+ ", shippingType=" + shippingType + ", SPAdress=" + SPAdress + ", PUAdress=" + PUAdress + "]";
	}
	
	
	
}
