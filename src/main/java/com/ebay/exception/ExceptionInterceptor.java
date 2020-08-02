package com.ebay.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ebay.dto.GameExceptionResponseDto;

@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionInterceptor.class);

	@ExceptionHandler(GameException.class)
	public final ResponseEntity<GameExceptionResponseDto> handleAllExceptions(GameException gx) {

		LOGGER.error(gx.getMessage(), gx);

		GameExceptionResponseDto exceptionResponse = new GameExceptionResponseDto(gx.getMessageResponse());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
