package com.srnr.vidhatasocietymlm.exception.customexceptions;

@SuppressWarnings("serial")
public class InvalidPhoneNumberAndPasswordException extends RuntimeException
{
	public InvalidPhoneNumberAndPasswordException(String message)
	{
		super(message);
	}

}
