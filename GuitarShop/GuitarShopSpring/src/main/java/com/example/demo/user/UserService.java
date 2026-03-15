package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.GuitarShopSpringApplication;
import jakarta.validation.ValidationException;
import model.User;

@Service
public class UserService {

    private final GuitarShopSpringApplication guitarShopSpringApplication;
	
	@Autowired
	UserRepo userRepo;

    UserService(GuitarShopSpringApplication guitarShopSpringApplication) {
        this.guitarShopSpringApplication = guitarShopSpringApplication;
    }

	public boolean saveUser(UserRegDTO user) {
		try {
			if(userRepo.findUserByUserName(user.getUserName()) != null)
				return false;
			User newUser = new User();
			newUser.setUserName(user.getUserName());
			newUser.setPassword(user.getUserPass());
			newUser.setType(user.getType());
			newUser.setUserMail(user.getUserMail());
			userRepo.save(newUser);
		}
		catch(ValidationException e) {
//			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public UserDTO findById(Integer userId) {
		try {
			UserDTO userDTO = new UserDTO();
			User user = userRepo.findById(userId).get();
			
			if(user == null)
				return null;
			
			userDTO.setUserId(user.getUserId());
			userDTO.setUserName(user.getUserName());
			userDTO.setUserPass(user.getPassword());
			userDTO.setUserMail(user.getUserMail());
			userDTO.setType(user.getType());
			
			return userDTO;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	public UserDTO findUserByName(String name) {
		UserDTO userDTO = null;
		try {
			userDTO = new UserDTO();
			User user = userRepo.findUserByUserName(name);
			if(user == null)
				return null;
			
			userDTO.setUserId(user.getUserId());
			userDTO.setUserName(user.getUserName());
			userDTO.setUserPass(user.getPassword());
			userDTO.setUserMail(user.getUserMail());
			userDTO.setType(user.getType());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return userDTO;
	}

	@Transactional
	public boolean updateName(Integer userId, String name) {
		try{
			User user = userRepo.findById(userId).get();
			if(user.getUserName() != name) {
				user.setUserName(name);
				return userRepo.save(user) != null;
			}
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Transactional
	public boolean updatePass(Integer userId, String pass) {
		try{
			User user = userRepo.findById(userId).get();
			if(user.getPassword() != pass) {
				user.setPassword(pass);
				return userRepo.save(user) != null;
			}
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Transactional
	public boolean updateMail(Integer userId, String mail) {
		try{
			User user = userRepo.findById(userId).get();
			if(user.getUserMail() != mail) {
				user.setUserMail(mail);
				return userRepo.save(user) != null;
			}
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public UserDTO createGuestUser() {
		User u = new User();
		int randomInt = (int) (Math.random() * 1000);
		u.setUserName("guest" + randomInt);
		u.setType("guest");
		u.setUserMail("dummy");
		u.setPassword("dummy");
		try {
			userRepo.save(u);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return new UserDTO(u.getUserId(), u.getUserName(), u.getPassword(), u.getType(), u.getUserMail());
	}

	public void removeUser(int userId) {
		try {
			User u = userRepo.findById(userId).get();
			userRepo.delete(u);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public String getUserNameAndMail(Integer userId) {
		try {
			User u = userRepo.findById(userId).get();
			return u.getUserMail()+":"+u.getUserName();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
