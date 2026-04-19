package com.example.demo.product;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.EmbeddedId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import model.OverviewPK;


@Validated
public class OverviewDTO {
	
	@NotNull
	private OverviewPK id;

	@NotNull
	private int rating;

	@NotNull
	private String text;
	
	@NotNull
	private String userName;

	public OverviewPK getId() {
		return id;
	}

	public void setId(OverviewPK id) {
		this.id = id;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public OverviewDTO(@Valid OverviewPK id, @Valid int rating, @Valid String text, @Valid String userName) {
		this.id = id;
		this.rating = rating;
		this.text = text;
		this.userName = userName;
	}

	public OverviewDTO() {
		
	}

	@Override
	public String toString() {
		return "OverviewDTO [id=" + id + ", rating=" + rating + ", text=" + text + "]";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
}
