package com.example.demo.product;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.EmbeddedId;
import model.OverviewPK;


@Validated
public class OverviewDTO {
	
	private OverviewPK id;

	private int rating;

	private String text;

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

	public OverviewDTO(OverviewPK id, int rating, String text) {
		this.id = id;
		this.rating = rating;
		this.text = text;
	}

	public OverviewDTO() {
		
	}

	@Override
	public String toString() {
		return "OverviewDTO [id=" + id + ", rating=" + rating + ", text=" + text + "]";
	}

	
}
