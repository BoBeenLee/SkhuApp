package com.skhu.bobinlee.skhuapp.model.data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Facebook implements Serializable {
	@JsonProperty("id")
	String id;
	@JsonProperty("picture")
	String picture;
	@JsonProperty("message")
	String message;
	@JsonProperty("link")
	String link;
	@JsonProperty("created_time")
	String create_time;
	
	public String getId() {
		return id;
	}
	public String getPicture() {
		return picture;
	}
	public String getMessage() {
		return message;
	}
	public String getLink() {
		return link;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
}
