package com.ebay.dto;

public class QuestionPossibleAnswerDto {

	private Integer id;
	private String possibleAnswer;
	
	public QuestionPossibleAnswerDto() {
		
	}

	public QuestionPossibleAnswerDto(Integer id, String possibleAnswer) {
		super();
		this.id = id;
		this.possibleAnswer = possibleAnswer;
	}

	public QuestionPossibleAnswerDto(String possibleAnswer) {
		super();
		this.possibleAnswer = possibleAnswer;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPossibleAnswer() {
		return possibleAnswer;
	}

	public void setPossibleAnswer(String possibleAnswer) {
		this.possibleAnswer = possibleAnswer;
	}

	@Override
	public String toString() {
		return "QuestionPossibleAnswerDto [id=" + id + ", possibleAnswer=" + possibleAnswer + "]";
	}
}
