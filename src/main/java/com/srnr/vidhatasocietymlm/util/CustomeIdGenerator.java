package com.srnr.vidhatasocietymlm.util;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class CustomeIdGenerator 
{
	public static String generateCustomId()
	{
		long timeStamp = Instant.now().toEpochMilli();
		int randomPart = ThreadLocalRandom.current().nextInt(100000000, 999999999);
	    return timeStamp+""+randomPart;	
	}
}
