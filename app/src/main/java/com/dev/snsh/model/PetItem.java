package com.dev.snsh.model;

import com.google.gson.annotations.SerializedName;

public class PetItem{

	@SerializedName("image")
	private String image;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	public String getImage(){
		return image;
	}

	public String getName(){
		return name;
	}

	public String getDescription(){
		return description;
	}

	public int getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}
}