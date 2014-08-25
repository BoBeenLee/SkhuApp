package com.skhu.bobinlee.skhuapp.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostForm <T> implements Serializable {
	@JsonProperty("posts")
	Posts<T> posts;
	@JsonProperty("id")
	String id;
	
	public Posts<T> getPosts() {
		return posts;
	}
	public void setPosts(Posts<T> posts) {
		this.posts = posts;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
