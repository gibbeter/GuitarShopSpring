package com.example.demo.product;

import org.springframework.validation.annotation.Validated;


@Validated
public class ProductDTO {
	
	private Integer prodId;
	
	private String productDesc;

	private String productName;

	private Integer productStock;

	private Integer productTypeId;
	
	private String productTypeName;
	
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
		this.productDesc = productDesc;
		this.productName = productName;
		this.productStock = productStock;
		this.productTypeId = productTypeId;
		this.productTypeName = productTypeName;
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
