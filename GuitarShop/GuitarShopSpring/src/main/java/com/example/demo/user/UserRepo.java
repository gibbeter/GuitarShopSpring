package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import model.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	User findUserByUserName(@Param("userName")String name);

}
