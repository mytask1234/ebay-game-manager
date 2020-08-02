package com.ebay.dto;

import java.util.List;

public class QuestionDto {

	private Integer id;
	private String questionText;
	private List<QuestionPossibleAnswerDto> questionPossibleAnswers;
	
	public QuestionDto() {
		
	}

	public QuestionDto(String questionText) {
		super();
		this.questionText = questionText;
	}
	
	public QuestionDto(String questionText, List<QuestionPossibleAnswerDto> questionPossibleAnswers) {
		super();
		this.questionText = questionText;
		this.questionPossibleAnswers = questionPossibleAnswers;
	}

	public QuestionDto(Integer id, String questionText, List<QuestionPossibleAnswerDto> questionPossibleAnswers) {
		super();
		this.id = id;
		this.questionText = questionText;
		this.questionPossibleAnswers = questionPossibleAnswers;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public List<QuestionPossibleAnswerDto> getQuestionPossibleAnswers() {
		return questionPossibleAnswers;
	}

	public void setQuestionPossibleAnswers(List<QuestionPossibleAnswerDto> questionPossibleAnswers) {
		this.questionPossibleAnswers = questionPossibleAnswers;
	}

	@Override
	public String toString() {
		return "QuestionDto [id=" + id + ", questionText=" + questionText + ", questionPossibleAnswers="
				+ questionPossibleAnswers + "]";
	}
}
