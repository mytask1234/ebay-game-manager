package com.ebay.dto;

public class UserAndPointsResponseDto {

	private String username;
	private Integer points;
	
	public UserAndPointsResponseDto(String username, Integer points) {
		super();
		this.username = username;
		this.points = points;
	}

	public String getUsername() {
		return username;
	}

	public Integer getPoints() {
		return points;
	}

	@Override
	public String toString() {
		return "UserAndPointsResponseDto [username=" + username + ", points=" + points + "]";
	}
}
