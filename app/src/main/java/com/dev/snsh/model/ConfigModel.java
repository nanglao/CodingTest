package com.dev.snsh.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ConfigModel{

	@SerializedName("config")
	private List<ConfigItem> config;

	public List<ConfigItem> getConfig(){
		return config;
	}

	@Override
	public String toString() {
		return "ConfigModel{" +
				"config=" + config +
				'}';
	}
}