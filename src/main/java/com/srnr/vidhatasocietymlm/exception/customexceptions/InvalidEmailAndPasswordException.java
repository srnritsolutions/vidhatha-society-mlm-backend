package com.srnr.vidhatasocietymlm.exception.customexceptions;

@SuppressWarnings("serial")
public class InvalidEmailAndPasswordException extends RuntimeException{
	public InvalidEmailAndPasswordException(String message)
	{
		super(message);
	}
}
