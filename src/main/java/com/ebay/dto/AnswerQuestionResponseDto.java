package com.ebay.dto;

public class AnswerQuestionResponseDto {

	private AnswerStatus answerStatus;
	private int pointsEarned;

	public AnswerQuestionResponseDto(AnswerStatus answerStatus) {
		super();
		this.answerStatus = answerStatus;
		pointsEarned = 0;
	}

	public AnswerQuestionResponseDto(AnswerStatus answerStatus, int pointsEarned) {
		super();
		this.answerStatus = answerStatus;
		this.pointsEarned = pointsEarned;
	}

	public AnswerStatus getAnswerStatus() {
		return answerStatus;
	}

	public int getPointsEarned() {
		return pointsEarned;
	}

	@Override
	public String toString() {
		return "AnswerQuestionResponseDto [answerStatus=" + answerStatus + ", pointsEarned=" + pointsEarned + "]";
	}
}
