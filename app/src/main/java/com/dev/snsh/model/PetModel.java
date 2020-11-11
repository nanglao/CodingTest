package com.dev.snsh.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PetModel{

	@SerializedName("Pet")
	private List<PetItem> pet;

	public List<PetItem> getPet(){
		return pet;
	}
}