package com.srnr.vidhatasocietymlm.apprunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class LevelCreation implements CommandLineRunner
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
		
	 @Override
	    public void run(String... args) throws Exception 
	 {
	        String tableName = "LEVEL_TABLE"; // Replace with your table name

	        // Query to check if the table exists
	        String query = "SELECT COUNT(*) FROM all_tables WHERE table_name = ?";

	        Integer count = jdbcTemplate.queryForObject(query, Integer.class, tableName.toUpperCase());

	        if (count != null && count == 0) 
	        {
	        	// SQL to create the table
	            String createTableSql = "CREATE TABLE " + tableName + " ("
	                    + "id VARCHAR2(50) PRIMARY KEY, "
	                    + "levelno NUMBER, "
	                    + "rewardname VARCHAR2(50))";

	            jdbcTemplate.execute(createTableSql);
	            
	        } 
	       
	    }

}
