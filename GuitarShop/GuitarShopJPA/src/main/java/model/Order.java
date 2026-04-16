package model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the order database table.
 * 
 */
@Entity
@Table(name="PurchaseOrder")
@NamedQuery(name="Order.findAll", query="SELECT o FROM Order o")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="order_id")
	private Integer orderId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="order_date")
	private Date orderDate;

	@Column(name="order_status")
	private String orderStatus;

	@Column(name="user_id")
	private Integer userId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="surname")
	private String surname;
	
	@Column(name="phone_number")
	private Integer phoneNumber;
	
	@Column(name="summ")
	private Integer summ;

	@Column(name="order_type")
	private String orderType;

    @Column(name="pu_adress")
	private String pickupAdress;

    @Column(name="sp_adress")
	private String shippingAdress;

    @Column(name="comp_time")
	private Date completionTime;

	//bi-directional many-to-one association to Orderitem
	@OneToMany(mappedBy="order")
	private List<Orderitem> orderitems;

	public Order() {
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getUserId() {
		return this.userId;
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

	public List<Orderitem> getOrderitems() {
		return this.orderitems;
	}

	public void setOrderitems(List<Orderitem> orderitems) {
		this.orderitems = orderitems;
	}

	public Orderitem addOrderitem(Orderitem orderitem) {
		getOrderitems().add(orderitem);
		orderitem.setOrder(this);

		return orderitem;
	}

	public Orderitem removeOrderitem(Orderitem orderitem) {
		getOrderitems().remove(orderitem);
		orderitem.setOrder(null);

		return orderitem;
	}
	
//	public Order(int orderId, Date orderDate, String orderStatus, int userId, List<Orderitem> orderitems) {
//		this.orderId = orderId;
//		this.orderDate = orderDate;
//		this.orderStatus = orderStatus;
//		this.userId = userId;
//		this.orderitems = orderitems;
//	}

	public Order(Integer orderId, Date orderDate, String orderStatus, Integer userId, Integer summ, String orderType,
			String pickupAdress, String shippingAdress, Date completionTime, List<Orderitem> orderitems) {
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
	
	
	
}