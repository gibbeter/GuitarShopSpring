package com.example.demo.order;

import java.util.Date;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import model.Cartitem;
import model.Orderitem;


@Validated
public class OrderDTO {
	
	@NotNull
	private Integer orderId;

	@NotNull
	@FutureOrPresent
	private Date orderDate;

	@NotNull
	private String orderStatus;

	@NotNull
	private Integer userId;
	
	@NotNull
	private String name;
	
	@NotNull
	private String surname;
	
	@NotNull
	private Integer phoneNumber;
	
	@NotNull
	private Integer summ;

	@NotNull
	private String orderType;

	@NotNull
	private String pickupAdress;

	@NotNull
	private String shippingAdress;

	private Date completionTime;
	
	@NotNull
	private List<OrderItemDTO> orderitems;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Integer getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<OrderItemDTO> getOrderitems() {
		return orderitems;
	}

	public void setOrderitems(List<OrderItemDTO> orderitems) {
		this.orderitems = orderitems;
	}
	
	public Integer getSumm() {
		return summ;
	}

	public void setSumm(Integer summ) {
		this.summ = summ;
	}
	
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPickupAdress() {
		return pickupAdress;
	}

	public void setPickupAdress(String pickupAdress) {
		this.pickupAdress = pickupAdress;
	}

	public String getShippingAdress() {
		return shippingAdress;
	}

	public void setShippingAdress(String shippingAdress) {
		this.shippingAdress = shippingAdress;
	}

	public Date getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}
	
	
	

	public OrderDTO(@NotNull Integer orderId, @NotNull @FutureOrPresent Date orderDate, @NotNull String orderStatus,
			@NotNull Integer userId, @NotNull String name, @NotNull String surname, @NotNull Integer phoneNumber,
			@NotNull Integer summ, @NotNull String orderType, @NotNull String pickupAdress,
			@NotNull String shippingAdress, Date completionTime, @NotNull List<OrderItemDTO> orderitems) {
		super();
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.userId = userId;
		this.name = name;
		this.surname = surname;
		this.phoneNumber = phoneNumber;
		this.summ = summ;
		this.orderType = orderType;
		this.pickupAdress = pickupAdress;
		this.shippingAdress = shippingAdress;
		this.completionTime = completionTime;
		this.orderitems = orderitems;
	}

	public OrderDTO(@NotNull Integer orderId, @NotNull @FutureOrPresent Date orderDate, @NotNull String orderStatus,
			@NotNull Integer userId, @NotNull Integer summ, String orderType, String pickupAdress,
			String shippingAdress, Date completionTime, @NotNull List<OrderItemDTO> orderitems) {
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.userId = userId;
		this.summ = summ;
		this.orderType = orderType;
		this.pickupAdress = pickupAdress;
		this.shippingAdress = shippingAdress;
		this.completionTime = completionTime;
		this.orderitems = orderitems;
	}

	public OrderDTO(@NotNull Integer orderId, @NotNull @FutureOrPresent Date orderDate, @NotNull String orderStatus,
			@NotNull Integer userId, @NotNull Integer summ) {
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.userId = userId;
		this.summ = summ;
	}

	public OrderDTO(@NotNull Integer orderId, @NotNull @FutureOrPresent Date orderDate, @NotNull String orderStatus,
			@NotNull Integer userId, @NotNull List<OrderItemDTO> orderitems) {
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.userId = userId;
		this.orderitems = orderitems;
	}

	public OrderDTO(@NotNull Integer orderId, @NotNull Date orderDate, @NotNull String orderStatus, @NotNull Integer userId) {
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.userId = userId;
	}
	
	public OrderDTO() {

	}

	@Override
	public String toString() {
		return "OrderDTO [orderId=" + orderId + ", orderDate=" + orderDate + ", orderStatus=" + orderStatus
				+ ", userId=" + userId + "]";
	}
	
	
	
}
