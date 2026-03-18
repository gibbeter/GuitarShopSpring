package model;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the overview database table.
 * 
 */
@Entity
@NamedQuery(name="Overview.findAll", query="SELECT o FROM Overview o")
public class Overview implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private OverviewPK id;

	private int rating;

	private String text;
	
	@Column(name = "user_name")
	private String userName;

	public Overview() {
	}

	public OverviewPK getId() {
		return this.id;
	}

	public void setId(OverviewPK id) {
		this.id = id;
	}

	public int getRating() {
		return this.rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}