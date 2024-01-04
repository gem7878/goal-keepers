package com.goalkeepers.server.exception;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(basePackages = "com.goalkeepers.server")
public class GlobalExceptionHandler {
    
    // @Valid Error
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		List<ErrorResponseDto.FieldError> errors = new ArrayList<>();
		for(FieldError fieldError : e.getFieldErrors()) {
			log.error("name:{}, message:{}", fieldError.getField(), fieldError.getDefaultMessage());
			ErrorResponseDto.FieldError error = new ErrorResponseDto.FieldError();
			error.setField(fieldError.getField());
			error.setMessage(fieldError.getDefaultMessage());

			errors.add(error);
		}

		ErrorResponseDto response = new ErrorResponseDto(ErrorCode.BAD_REQUEST, errors);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/**
	 * JPA를 통해 DB 조작시 발생
	 * ConstraintViolationException : 제약 조건 위배되었을 때 발생
	 * DataIntegrityViolationException : 데이터의 삽입/수정이 무결성 제약 조건을 위반할 때 발생
	 * 
	 */
	@ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
	protected ResponseEntity<ErrorResponseDto> handleDataException(Exception e) {
		ErrorResponseDto response = new ErrorResponseDto(ErrorCode.DUPLICATE_RESOURCE);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/**
	 * enum type 일치하지 않아 binding 못할 경우 발생
	 * 주로 @RequestParam enum으로 binding 못했을 경우 발생
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		ErrorResponseDto response = new ErrorResponseDto(ErrorCode.BAD_REQUEST);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/**
	 * 지원하지 않은 HTTP method 호출 할 경우 발생
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		ErrorResponseDto response = new ErrorResponseDto(ErrorCode.METHOD_NOT_ALLOWED);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/**
	 * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생
	 */
	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException e) {
		ErrorResponseDto response = new ErrorResponseDto(ErrorCode.ACCESS_DENIED);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/**
	 * 로그인 중 email 또는 password가 다른 경우
	 */
	@ExceptionHandler(BadCredentialsException.class)
	protected ResponseEntity<ErrorResponseDto> handleBadCredentialException(BadCredentialsException e) {
		ErrorResponseDto response = new ErrorResponseDto(ErrorCode.BAD_CREDENTIALS);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/**
	 * @RequestParmas null error
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	protected ResponseEntity<ErrorResponseDto> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
		ErrorResponseDto response = new ErrorResponseDto(ErrorCode.BAD_REQUEST, "RequestParams가 비어있습니다.");
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/**
	 *  파일 에러
	 */
	@ExceptionHandler(value = IOException.class)
	protected ResponseEntity<ErrorResponseDto> handleIOException(IOException e) {
		ErrorResponseDto response = new ErrorResponseDto(ErrorCode.FILE_ERROR);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/**
	 * 파일을 못 찾을 때
	 */
	@ExceptionHandler(value = MalformedURLException.class)
	protected ResponseEntity<ErrorResponseDto> handleMalformedURLException(MalformedURLException e) {
		ErrorResponseDto response = new ErrorResponseDto(ErrorCode.FILE_NOT_FOUND);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/**
	 * Business Logic 수행 중 발생시킬 커스텀 에러
	 */
	@ExceptionHandler(value = { CustomException.class })
	protected ResponseEntity<ErrorResponseDto> handleCustomException(CustomException e) {
		ErrorResponseDto response = new ErrorResponseDto(ErrorCode.BAD_REQUEST, e.getMessage());
		return ResponseEntity.status(response.getStatus()).body(response);
	}
	
	/**
	 * 위에 해당하는 예외에 해당하지 않을 때 모든 예외를 처리하는 메소드
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponseDto> handleException(Exception e) {
		e.printStackTrace();
		ErrorResponseDto response = new ErrorResponseDto(ErrorCode.SERVER_ERROR);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

}
