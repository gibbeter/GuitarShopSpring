package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserHelper {
	@Autowired
	UserService userService;
	
	public UserDTO checkIsUserAlive(Integer userId) {
		try {
			UserDTO user = userService.findById(userId);
			if (user != null)
				return user;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
