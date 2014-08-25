package com.skhu.bobinlee.skhuapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Posts <T> implements Serializable {
	ArrayList<T> data;
	@JsonIgnore
	private HashMap<String, String> paging;

	@JsonCreator
	public Posts(@JsonProperty("data") ArrayList<T> data) {
		this.data = data;
	}
	
	public ArrayList<T> getData() {
		return data;
	}
	public void setData(ArrayList<T> data) {
		this.data = data;
	}
	public HashMap<String, String> getPaging() {
		return paging;
	}
	public void setPaging(HashMap<String, String> paging) {
		this.paging = paging;
	}
}
