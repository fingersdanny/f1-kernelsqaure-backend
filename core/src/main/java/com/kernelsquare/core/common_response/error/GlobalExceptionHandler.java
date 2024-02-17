package com.kernelsquare.core.common_response.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.common_response.error.exception.InvalidParamException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse> onBusinessException(BusinessException e) {
		return ResponseEntityFactory.toResponseEntity(e.getErrorCode());
	}

	@ExceptionHandler(InvalidParamException.class)
	public ResponseEntity<ApiResponse> onInvalidParamException(InvalidParamException e) {
		return ResponseEntityFactory.toResponseEntity(e.getErrorCode());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return ResponseEntityFactory.toResponseEntity(e);
	}
}
