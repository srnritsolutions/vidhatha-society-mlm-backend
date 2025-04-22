package com.srnr.vidhatasocietymlm.user.dao;

import java.util.Optional;

import com.srnr.vidhatasocietymlm.model.User;

public interface UserDAO 
{
	Optional<User> saveUser(User user,String referralCode);
	Optional<User> updateUserAfterPaymentSuccess(String userId, boolean paymentSuccess);
	Optional<User> loginByEmailAndPassword(String userEmail, String userPassword);
	Optional<User> updatePassword(String userEmail, String newPassword);
}
