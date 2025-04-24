package com.srnr.vidhatasocietymlm.exception.customexceptions;

@SuppressWarnings("serial")
public class UserAlreadyExistException extends RuntimeException {
	public UserAlreadyExistException(String msg) {
		super(msg);
	}

	
}
