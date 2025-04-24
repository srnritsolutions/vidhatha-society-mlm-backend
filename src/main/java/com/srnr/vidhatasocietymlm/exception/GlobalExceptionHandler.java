package com.srnr.vidhatasocietymlm.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.srnr.vidhatasocietymlm.exception.customexceptions.InvalidEmailAndPasswordException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.InvalidReferralException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.InvalideOTPException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UnSupportedFileTypeException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserAlreadyExistException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserNotFoundException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserNotcreatedException;
import com.srnr.vidhatasocietymlm.util.Message;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

	@ExceptionHandler(UserNotcreatedException.class)
	public ResponseEntity<Message> userNotCreatedException(UserNotcreatedException e) {
		return buildErrorResponse(e);
	}

	@ExceptionHandler(InvalidEmailAndPasswordException.class)
	public ResponseEntity<Message> invalidEmailAndPasswordException(InvalidEmailAndPasswordException e) {
		return buildErrorResponse(e);
	}

	@ExceptionHandler(InvalideOTPException.class)
	public ResponseEntity<Message> invalideOTPException(InvalideOTPException e) {
		return buildErrorResponse(e);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Message> userNotFoundException(UserNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(e.getMessage()));
	}


	@ExceptionHandler(InvalidReferralException.class)
	public ResponseEntity<Message> invalidReferralException(InvalidReferralException e) {
		return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Message> runtimeException(RuntimeException e) {
		return buildErrorResponse(e);
	}

	// Common method to avoid duplication
	private ResponseEntity<Message> buildErrorResponse(Exception e, HttpStatus status) {
		return ResponseEntity.status(status).body(new Message(e.getMessage()));
	}

	// Common method to avoid duplication
	private ResponseEntity<Message> buildErrorResponse(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<Message> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
				.body(new Message("The content type is not supported: " + ex.getMessage()));
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException e) {
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
				.body(new Message("File size exceeds the maximum limit 10MB!"));
	}

	@ExceptionHandler({ UserAlreadyExistException.class, NoResourceFoundException.class,
			UnSupportedFileTypeException.class, IllegalArgumentException.class

	})
	public ResponseEntity<?> handleBadRequestException(Exception e) {
		return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
	}

}
