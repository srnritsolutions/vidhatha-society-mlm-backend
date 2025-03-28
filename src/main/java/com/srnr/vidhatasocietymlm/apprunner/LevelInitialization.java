package com.srnr.vidhatasocietymlm.apprunner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.srnr.vidhatasocietymlm.model.Level;
import com.srnr.vidhatasocietymlm.repository.LevelRepository;

@Component
@Order(value = 2)
public class LevelInitialization implements CommandLineRunner
{
 
	@Autowired
	private LevelRepository levelRepository;
	
	@Override
	public void run(String... args) throws Exception 
	{
		if (levelRepository.count() == 0) {
            List<Level> levels = List.of(
                new Level("L_01",0, "Small Plant"),
                new Level("L_02",1, null),
                new Level("L_03",2, null),
                new Level("L_04",3, null),
                new Level("L_05",4, null),
                new Level("L_06",5, null),
                new Level("L_07",6, "Smart Phone"),
                new Level("L_08",7, "Refrigerator"),
                new Level("L_09",8, "Laptop"),
                new Level("L_010",9, "Motor Bike"),
                new Level("L_011",10, "Tata Tiago Car"),
                new Level("L_012",11, "Swift Dzire Car"),
                new Level("L_013",12, "Mahindra Thar Car")
            );

            levelRepository.saveAll(levels);
        } 
		

		
	}

}
