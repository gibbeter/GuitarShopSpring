package com.example.demo.product;

import org.springframework.validation.annotation.Validated;


@Validated
public class ProductDTO {
	
	private int prodId;
	
	private String productDesc;

	private String productName;

	private int productStock;

	private int productTypeId;
	
	private String productTypeName;
	
	private int productPrice;

	public int getProdId() {
		return prodId;
	}

	public void setProdId(int prodId) {
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

	public int getProductStock() {
		return productStock;
	}

	public void setProductStock(int stok) {
		this.productStock = stok;
	}

	public int getProductType() {
		return productTypeId;
	}

	public void setProductType(int type) {
		this.productTypeId = type;
	}
	
	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public ProductDTO(int prodId, String productDesc, String productName, int productStock, int productTypeId,
			String productTypeName, int price) {
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

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int price) {
		this.productPrice = price;
	}

	

}
