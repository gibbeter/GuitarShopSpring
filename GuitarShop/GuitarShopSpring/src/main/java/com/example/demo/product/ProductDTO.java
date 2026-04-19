package com.example.demo.product;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Validated
public class ProductDTO {
	
	@NotNull
	private Integer prodId;
	
	@Size(min = 0, max = 65535, message="Description is too long")
	private String productDesc;

	@NotNull
	@Size(min = 0, max = 255, message="Name is too long")
	private String productName;

	@NotNull
	@Min(0)
	private Integer productStock;

	@NotNull
	private Integer productTypeId;
	
	@NotNull
	@Size(min = 0, max = 255, message="Type name is too long")
	private String productTypeName;
	
	@NotNull
	@Min(0)
	private Integer productPrice;
	
	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductStock() {
		return productStock;
	}

	public void setProductStock(Integer stok) {
		this.productStock = stok;
	}

	public Integer getProductType() {
		return productTypeId;
	}

	public void setProductType(Integer type) {
		this.productTypeId = type;
	}
	
	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public ProductDTO(Integer prodId, String productDesc, String productName, Integer productStock, Integer productTypeId,
			String productTypeName, Integer price) {
		this.prodId = prodId;
		this.productName = productName;
		this.productDesc = productDesc;
		this.productTypeId = productTypeId;
		this.productTypeName = productTypeName;
		this.productStock = productStock;
		this.productPrice = price;
	}
	
	public ProductDTO(String productName,String productDesc, Integer productTypeId,
			String productTypeName, Integer productStock, Integer price) {
		this.productName = productName;
		this.productDesc = productDesc;
		this.productTypeId = productTypeId;
		this.productTypeName = productTypeName;
		this.productStock = productStock;
		this.productPrice = price;
	}

	public ProductDTO() {
		
	}
	

	@Override
	public String toString() {
		return "ProductDTO [prodId=" + prodId + ", productDesc=" + productDesc + ", productName=" + productName
				+ ", productStock=" + productStock + ", productTypeId=" + productTypeId + ", productTypeName="
				+ productTypeName + ", price=" + productPrice + "]";
	}

	public Integer getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Integer price) {
		this.productPrice = price;
	}

	

}
