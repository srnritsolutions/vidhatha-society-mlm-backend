package com.srnr.vidhatasocietymlm.user.dao;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.srnr.vidhatasocietymlm.model.User;

public interface UserDAO 
{
	Optional<User> saveUser(User user,String referralCode);
	Optional<User> updateUserAfterPaymentSuccess(String userId, boolean paymentSuccess);
	Optional<User> findByUserEmail(String userEmail);
	Optional<User> findByUserPhoneNumber(Long userPhoneNumber);
	Optional<User> loginByEmailAndPassword(String userEmail, String userPassword);
	Optional<User> loginByPhoneNumberAndPassword(Long phoneNumber, String userPassword);
	Optional<User> updatePassword(String userEmail, String newPassword);
	Optional<User> editImage(MultipartFile file,String userId);
	Optional<User> findByUserId(String id);
	Optional<User> updateByUserId(User user,String userId);
}
