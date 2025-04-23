package com.srnr.vidhatasocietymlm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.srnr.vidhatasocietymlm.appconstants.Role;
import com.srnr.vidhatasocietymlm.model.User;

import jakarta.persistence.LockModeType;
@Repository
public interface UserRepository extends JpaRepository<User, String>
{
	User getById(String id);
	
	Optional<User> findByEmail(String email);
	User findByPhoneNumber(Long phoneNumber);
	List<User> findByParent(User parent);
	List<User> findByRole(Role role);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select u from User u where u.id=:id")
	Optional<User> lockByUserId(@Param("id") String id);
	Optional<User> findByEmailAndPassword(String email, String password);

	
}

