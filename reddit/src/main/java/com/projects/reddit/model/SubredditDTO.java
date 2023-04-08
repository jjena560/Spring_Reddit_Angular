package com.projects.reddit.model;

import lombok.Data;

@Data
public class SubredditDTO {

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSubredditName() {
		return subredditName;
	}
	public void setSubredditName(String subredditName) {
		this.subredditName = subredditName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getNumberOfPosts() {
		return numberOfPosts;
	}
	public void setNumberOfPosts(Integer numberOfPosts) {
		this.numberOfPosts = numberOfPosts;
	}
	private Long id;
	private String subredditName;
	private String description;
	private Integer numberOfPosts;
	
}
