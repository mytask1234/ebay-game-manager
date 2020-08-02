package com.ebay.dto;

import java.util.List;

public class LeaderboardResponseDto {

	private List<UserAndPointsResponseDto> userAndPointsPairs;

	public LeaderboardResponseDto(List<UserAndPointsResponseDto> userAndPointsPairs) {
		super();
		this.userAndPointsPairs = userAndPointsPairs;
	}

	public List<UserAndPointsResponseDto> getUserAndPointsPairs() {
		return userAndPointsPairs;
	}

	public void setUserAndPointsPairs(List<UserAndPointsResponseDto> userAndPointsPairs) {
		this.userAndPointsPairs = userAndPointsPairs;
	}

	@Override
	public String toString() {
		return "LeaderboardResponseDto [userAndPointsPairs=" + userAndPointsPairs + "]";
	}
}
