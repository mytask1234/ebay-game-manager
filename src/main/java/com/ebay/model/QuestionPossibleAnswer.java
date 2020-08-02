package com.ebay.model;

import java.util.concurrent.atomic.AtomicInteger;

public class QuestionPossibleAnswer {

	private int id;
	private String possibleAnswer;
	private AtomicInteger totalUserAnswered;
	
	public QuestionPossibleAnswer(int id, String possibleAnswer) {
		super();
		this.id = id;
		this.possibleAnswer = possibleAnswer;
		totalUserAnswered = new AtomicInteger(0);
	}
	
	public int getId() {
		return id;
	}
	
	public void incTotalUserAnswered() {
		
		totalUserAnswered.incrementAndGet();
	}
	
	public int getTotalUserAnswered() {
		
		return totalUserAnswered.get();
	}

	public String getPossibleAnswer() {

		return possibleAnswer;
	}

	@Override
	public String toString() {
		return "QuestionPossibleAnswer [id=" + id + ", possibleAnswer=" + possibleAnswer + ", totalUserAnswered="
				+ totalUserAnswered + "]";
	}
}
