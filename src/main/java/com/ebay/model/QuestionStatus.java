package com.ebay.model;

public enum QuestionStatus {

	RESOLVED("Resolved"),
	PENDING("Pending"),
	UNRESOLVED("Unresolved");

	private String status;

	QuestionStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return status;
	}
}
