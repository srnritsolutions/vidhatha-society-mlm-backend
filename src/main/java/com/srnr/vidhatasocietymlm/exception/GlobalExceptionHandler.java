package com.srnr.vidhatasocietymlm.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.srnr.vidhatasocietymlm.exception.customexceptions.InvalidReferralException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserAlreadyExistEmailException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserAlreadyExistPhoneNumberException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserNotFoundException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserNotcreatedException;
import com.srnr.vidhatasocietymlm.util.Message;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) 
	{
		Map<String, String> errors = new HashMap<>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

	@ExceptionHandler(UserNotcreatedException.class)
	public ResponseEntity<Message> userNotCreatedException(UserNotcreatedException e) 
	{
		return buildErrorResponse(e);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Message> userNotFoundException(UserNotFoundException e) 
	{
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(e.getMessage()));
	}

	@ExceptionHandler({ NoResourceFoundException.class,
		                IllegalArgumentException.class })
	public ResponseEntity<Message> handleBadRequestException(Exception e) 
	{
		return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidReferralException.class)
	public ResponseEntity<Message> invalidReferralException(InvalidReferralException e) 
	{
		return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserAlreadyExistEmailException.class)
	public ResponseEntity<Message> userAlreadyExistEmailException(UserAlreadyExistEmailException e)
	{
		return buildErrorResponse(e, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UserAlreadyExistPhoneNumberException.class)
	public ResponseEntity<Message> userAlreadyExistPhoneNumberException(UserAlreadyExistPhoneNumberException e)
	{
		return buildErrorResponse(e, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Message> runtimeException(RuntimeException e)
	{
		return buildErrorResponse(e);
	}

	// Common method to avoid duplication
	private ResponseEntity<Message> buildErrorResponse(Exception e, HttpStatus status) 
	{
		return ResponseEntity.status(status).body(new Message(e.getMessage()));
	}

	// Common method to avoid duplication
	private ResponseEntity<Message> buildErrorResponse(Exception e) 
	{
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
	}

}
