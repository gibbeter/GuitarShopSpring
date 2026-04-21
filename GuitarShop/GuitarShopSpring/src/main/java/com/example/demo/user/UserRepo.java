package com.example.demo.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import model.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	Optional<User> findUserByUserName(@Param("userName")String name);

	List<User> findByType(String role);

}
