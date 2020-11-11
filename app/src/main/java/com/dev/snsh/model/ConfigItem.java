package com.dev.snsh.model;

import com.google.gson.annotations.SerializedName;

public class ConfigItem{

	@SerializedName("image")
	private String image;

	@SerializedName("open_hour_from")
	private String openHourFrom;

	@SerializedName("name")
	private String name;

	@SerializedName("rating")
	private int rating;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("open_hour_to")
	private String openHourTo;

	public String getImage(){
		return image;
	}

	public String getOpenHourFrom(){
		return openHourFrom;
	}

	public String getName(){
		return name;
	}

	public int getRating(){
		return rating;
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

	public String getOpenHourTo(){
		return openHourTo;
	}

	@Override
	public String toString() {
		return "ConfigItem{" +
				"image='" + image + '\'' +
				", openHourFrom='" + openHourFrom + '\'' +
				", name='" + name + '\'' +
				", rating=" + rating +
				", description='" + description + '\'' +
				", id=" + id +
				", title='" + title + '\'' +
				", openHourTo='" + openHourTo + '\'' +
				'}';
	}
}