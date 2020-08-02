package com.ebay.dto;

public class AnswerQuestionRequestDto {

	private Integer questionId;
	private Integer userChoosedAnswerId;

	public AnswerQuestionRequestDto() {
		
	}

	public AnswerQuestionRequestDto(Integer questionId, Integer userChoosedAnswerId) {
		super();
		this.questionId = questionId;
		this.userChoosedAnswerId = userChoosedAnswerId;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer getUserChoosedAnswerId() {
		return userChoosedAnswerId;
	}

	public void setUserChoosedAnswerId(Integer userChoosedAnswerId) {
		this.userChoosedAnswerId = userChoosedAnswerId;
	}

	@Override
	public String toString() {
		return "AnswerQuestionRequestDto [questionId=" + questionId + ", userChoosedAnswerId=" + userChoosedAnswerId + "]";
	}
}
