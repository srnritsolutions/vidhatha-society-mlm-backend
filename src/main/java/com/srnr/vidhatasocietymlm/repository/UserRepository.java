package com.srnr.vidhatasocietymlm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srnr.vidhatasocietymlm.appconstants.Role;
import com.srnr.vidhatasocietymlm.model.User;

import jakarta.persistence.LockModeType;

public interface UserRepository extends JpaRepository<User, String>
{
	Optional<User> findById(String id);
	Optional<User> findByEmail(String email);
	Optional<User> findByPhoneNumber(Long phoneNumber);
	List<User> findByParent(User parent);
	List<User> findByRole(Role role);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select u from User u where u.id=:id")
	Optional<User> lockByUserId(@Param("id") String id);
	
}

