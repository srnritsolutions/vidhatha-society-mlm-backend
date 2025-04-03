package com.srnr.vidhatasocietymlm.exception.customexceptions;

@SuppressWarnings("serial")
public class AddressAlreadyExistException extends RuntimeException
{
	public AddressAlreadyExistException(String message) 
	{
		super(message);
		
	}
}

