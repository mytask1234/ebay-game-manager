package com.ebay.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AnswerStatus {

	CORRECT("Correct"),
	WRONG("Wrong"),
	PENDING("Pending"),
	UNRESOLVED("Unresolved"),
	ALREADY_ANSWERED("Already Answered"),
	UNKNOWN("Unknown");

	private String status;

	AnswerStatus(String status) {
		this.status = status;
	}

	@JsonValue
	@Override
	public String toString() {
		return status;
	}
}
