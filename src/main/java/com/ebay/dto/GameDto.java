package com.ebay.dto;

import java.util.List;

public class GameDto {

	private Integer id;
	private Integer pointsForCorrectAnswer;
	private List<QuestionDto> questions;
	
	public GameDto() {
		
	}
	
	public GameDto(Integer pointsForCorrectAnswer, List<QuestionDto> questions) {
		super();
		this.pointsForCorrectAnswer = pointsForCorrectAnswer;
		this.questions = questions;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPointsForCorrectAnswer() {
		return pointsForCorrectAnswer;
	}

	public void setPointsForCorrectAnswer(Integer pointsForCorrectAnswer) {
		this.pointsForCorrectAnswer = pointsForCorrectAnswer;
	}

	public List<QuestionDto> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDto> questions) {
		this.questions = questions;
	}

	@Override
	public String toString() {
		return "GameDto [id=" + id + ", pointsForCorrectAnswer=" + pointsForCorrectAnswer + ", questions=" + questions
				+ "]";
	}
}
