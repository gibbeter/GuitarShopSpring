package model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the product database table.
 * 
 */
@Entity
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="prod_id")
	private int prodId;

	@Column(name="product_desc")
	private String productDesc;

	@Column(name="product_name")
	private String productName;

	private int stock;

	//bi-directional many-to-one association to Cartitem
	@OneToMany(mappedBy="product")
	private List<Cartitem> cartitems;

	//bi-directional many-to-one association to Type
	@ManyToOne
	@JoinColumn(name="type")
	private Type typeBean;

	public Product() {
	}

	public int getProdId() {
		return this.prodId;
	}

	public void setProdId(int prodId) {
		this.prodId = prodId;
	}

	public String getProductDesc() {
		return this.productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getStock() {
		return this.stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public List<Cartitem> getCartitems() {
		return this.cartitems;
	}

	public void setCartitems(List<Cartitem> cartitems) {
		this.cartitems = cartitems;
	}

	public Cartitem addCartitem(Cartitem cartitem) {
		getCartitems().add(cartitem);
		cartitem.setProduct(this);

		return cartitem;
	}

	public Cartitem removeCartitem(Cartitem cartitem) {
		getCartitems().remove(cartitem);
		cartitem.setProduct(null);

		return cartitem;
	}

	public Type getTypeBean() {
		return this.typeBean;
	}

	public void setTypeBean(Type typeBean) {
		this.typeBean = typeBean;
	}

}