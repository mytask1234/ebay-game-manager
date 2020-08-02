package com.ebay.dto;

public class GameExceptionResponseDto {

	private String message;

	public GameExceptionResponseDto(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
