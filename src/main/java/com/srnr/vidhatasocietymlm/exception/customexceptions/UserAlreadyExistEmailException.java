package com.srnr.vidhatasocietymlm.exception.customexceptions;

@SuppressWarnings("serial")
public class UserAlreadyExistEmailException extends RuntimeException
{
	public UserAlreadyExistEmailException(String message) 
	{
		super(message);
	}
}

