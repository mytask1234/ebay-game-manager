package com.ebay.exception;

public class GameException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4744858762248583172L;

	private String messageResponse;

	public GameException() {

	}

	public GameException(String message) {
		super(message);
		this.messageResponse = message;
	}

	public GameException(Throwable cause) {
		super(cause);
	}

	public GameException(String message, Throwable cause) {
		super(message, cause);
		this.messageResponse = message;
	}

	public GameException(String message, String messageResponse) {
		super(message);
		this.messageResponse = messageResponse;
	}
	
	public GameException(String message, String messageResponse, Throwable cause) {
		super(message, cause);
		this.messageResponse = messageResponse;
	}
/*
	public GameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}*/

	public String getMessageResponse() {
		return messageResponse;
	}
}
