package com.example.demo.product;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.EmbeddedId;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import model.OverviewPK;


@Validated
public class CreateOverviewDTO {
	

	@NotNull
	@Min(1) @Max(5)
	private int rating;

	@NotNull
	@NotBlank(message = "Your overview has to have some text")
    @Size(min = 1, max = 500)
	private String text;
	

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

	public CreateOverviewDTO(int rating, String text) {
		this.rating = rating;
		this.text = text;
	}

	public CreateOverviewDTO() {
		
	}

	@Override
	public String toString() {
		return "OverviewDTO [rating=" + rating + ", text=" + text + "]";
	}

	
}
