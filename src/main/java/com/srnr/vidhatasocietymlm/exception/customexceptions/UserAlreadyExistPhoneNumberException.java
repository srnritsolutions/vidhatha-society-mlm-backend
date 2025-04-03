package com.srnr.vidhatasocietymlm.exception.customexceptions;

@SuppressWarnings("serial")
public class UserAlreadyExistPhoneNumberException extends RuntimeException
{
	public UserAlreadyExistPhoneNumberException(String message)
	{
		super(message);
	}
}

