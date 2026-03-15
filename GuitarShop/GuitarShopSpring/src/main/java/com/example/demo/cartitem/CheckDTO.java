package com.example.demo.cartitem;

import model.Cartitem;

public class CheckDTO {
		private Integer userId;
		private Integer productId;
		private String productName;
		private Integer quantity;
		private Integer productPrice;

		public CheckDTO() {
		}

		public CheckDTO(Cartitem item, Integer userId) {
			this.userId = userId;
			this.productId = item.getId().getProdId();
			this.productName = item.getProduct().getProductName();
			this.quantity = item.getQuantity();
			this.productPrice = item.getProduct().getPrice();

		}

	    public Integer getUserId() {
	        System.out.println("getUserId called: " + userId); // Отладка
	        return userId;
	    }

	    public Integer getProductId() {
	        return productId;
	    }

	    public String getProductName() {
	        return productName;
	    }

	    public Integer getQuantity() {
	        return quantity;
	    }

	    public Integer getProductPrice() {
	        return productPrice;
	    }

	    public void setUserId(Integer userId) {
	        this.userId = userId;
	    }
	    
	    public void setProductId(Integer productId) {
	        this.productId = productId;
	    }
	    
	    public void setProductName(String productName) {
	        this.productName = productName;
	    }
	    
	    public void setQuantity(Integer quantity) {
	        this.quantity = quantity;
	    }
	    
	    public void setProductPrice(Integer productPrice) {
	        this.productPrice = productPrice;
	    }
		
		@Override
	    public String toString() {
	        return "CheckDTO{" +
	            "userId=" + userId +
	            ", productName='" + productName + '\'' +
	            ", quantity=" + quantity +
	            ", price=" + productPrice +
	            '}';
	    }
}
