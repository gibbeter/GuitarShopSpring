package com.example.demo.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TypeDTO {
	
	private int typeId;
	
	@NotNull
	@NotBlank(message="Type name cant blank")
	@Size(max=255, message="Type name is too long")
	private String typeName;

	public TypeDTO(int id, String name) {
		this.typeId = id;
		this.typeName = name;
	}
	
	public TypeDTO() {
		
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "TypeDTO [typeId=" + typeId + ", typeName=" + typeName + "]";
	}
	
	
}
