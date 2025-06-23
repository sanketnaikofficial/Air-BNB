package com.SecurityAuthConfig.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SecurityAuthConfig.Entity.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long>{
	
	//Finder Methods
	User findByUsername(String username);
	
	User findByEmail(String email);
	
	boolean existsByUsername(String username);
	
	boolean existsByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.email = :username")
	User findByEmailId(@Param("username") String username);
	
	Optional<User> findByMobile(String key);
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	Optional<User> findByEmailIdforOTP(@Param("email") String email);
	
	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.mobile = :mobile WHERE u.username = :username")
	int updateMobileByUsername(@Param("mobile") String mobile, @Param("username") String username);


}