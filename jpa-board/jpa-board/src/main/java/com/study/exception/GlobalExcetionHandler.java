package com.study.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

// 전역 예외 핸들링용 
@RestControllerAdvice
@Slf4j
public class GlobalExcetionHandler {
	
//	@ExceptionHandler(RuntimeException.class)
//	public String handleRuntimeException(final RuntimeException e) {
//		log.error("handleRuntimeExcetion : {}", e.getMessage());
//		return e.getMessage();
//	}
	
	// Custom Exception
	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ErrorResponse> handleCustomException(final CustomException e) {
		log.error("handleCustomException: {}", e.getErrorCode());
		return ResponseEntity
				.status(e.getErrorCode().getStatus().value())
				.body(new ErrorResponse(e.getErrorCode()));
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
		log.error("handleHttpRequestMethodNotSupportedException: {}", e.getMessage());
		return ResponseEntity
				.status(ErrorCode.METHOD_NOT_ALLOWED.getStatus().value())
				.body(new ErrorResponse(ErrorCode.METHOD_NOT_ALLOWED));
	}
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(final Exception e) {
		log.error("handleException: {}", e.getMessage());
		return ResponseEntity
				.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value())
				.body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
	}

}
