package com.srnr.vidhatasocietymlm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.srnr.vidhatasocietymlm.model.Earnings;


public interface EarningsRepository extends JpaRepository<Earnings, String>
{
	Optional<Earnings> findById(String id);

}
