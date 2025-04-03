package com.srnr.vidhatasocietymlm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.srnr.vidhatasocietymlm.model.Referral;


public interface ReferralRepository extends JpaRepository<Referral, String>
{
	Optional<Referral> findByReferalCode(String referalCode);

}
