package com.srnr.vidhatasocietymlm.exception.customexceptions;

@SuppressWarnings("serial")
public class UnSupportedFileTypeException extends RuntimeException
{
	public UnSupportedFileTypeException(String msg)
	{
		super(msg);
	}
}
