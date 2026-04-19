package com.example.demo.user;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.GuitarShopSpringApplication;
import com.example.demo.files.BackUpService;
import com.example.demo.order.OrderDTO;

import jakarta.validation.ValidationException;
import model.Order;
import model.User;

@Service
public class UserService {

//    private final GuitarShopSpringApplication guitarShopSpringApplication;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	BackUpService backupService;


//    UserService(GuitarShopSpringApplication guitarShopSpringApplication) {
//        this.guitarShopSpringApplication = guitarShopSpringApplication;
//    }
    
    public UserDTO userToDTO(User u) {
    	return new UserDTO(u.getUserId(), u.getUserName(), u.getPassword(), u.getType(), u.getUserMail(), u.getName(), u.getSurname(), u.getPhoneNumber());
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
			newUser.setName(user.getName());
			newUser.setSurname(user.getSurname());
			newUser.setPhoneNumber(user.getPhoneNumber());
			userRepo.save(newUser);
			
			try {
				saveToCSV();
				return true;
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		catch(ValidationException e) {
//			e.printStackTrace();
			return false;
		}
	}
	
	public UserDTO findById(Integer userId) {
		try {
			UserDTO userDTO = new UserDTO();
			User user = userRepo.findById(userId).get();
			
			if(user == null)
				return null;
			
			userDTO = userToDTO(user);
//			userDTO.setUserId(user.getUserId());
//			userDTO.setUserName(user.getUserName());
//			userDTO.setUserPass(user.getPassword());
//			userDTO.setUserMail(user.getUserMail());
//			userDTO.setType(user.getType());
			
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
			
			userDTO = userToDTO(user);
//			userDTO.setUserId(user.getUserId());
//			userDTO.setUserName(user.getUserName());
//			userDTO.setUserPass(user.getPassword());
//			userDTO.setUserMail(user.getUserMail());
//			userDTO.setType(user.getType());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return userDTO;
	}

	@Transactional
	public boolean updateUserName(Integer userId, String name) {
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
			saveToCSV();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return userToDTO(u);
	}

	public void removeUser(int userId) {
		try {
			User u = userRepo.findById(userId).get();
			userRepo.delete(u);
			
			saveToCSV();
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

	@Transactional
	public boolean updateName(Integer userId, String name) {
		try{
			User user = userRepo.findById(userId).get();
			if(user.getName() != name) {
				user.setName(name);
				return userRepo.save(user) != null;
			}
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Transactional
	public boolean updateSurname(Integer userId, String surname) {
		try{
			User user = userRepo.findById(userId).get();
			if(user.getSurname() != surname) {
				user.setSurname(surname);
				return userRepo.save(user) != null;
			}
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Transactional
	public boolean updatePhoneNumber(Integer userId, Integer phoneNumber) {
		try{
			User user = userRepo.findById(userId).get();
			if(user.getPhoneNumber() != phoneNumber) {
				user.setPhoneNumber(phoneNumber);
				return userRepo.save(user) != null;
			}
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		//throw Exc on short number
	}
	
	
	
	private void saveToCSV(){

        try{
        	List<UserDTO> usersList = usersToDTO(userRepo.findAll());
    		String prefix = "users";
    		String fileName = "users";
            String[] header = {"user_id","type","user_name","password","name",
            					"name","surname","phone_number","user_mail"};
            
            Function<UserDTO, String[]> userMapper = user -> new String[]{
            		safeString(user.getUserId()),
            		safeString(user.getType()),
            	    safeString(user.getUserName()),
            	    safeString(user.getUserPass()),
            	    safeString(user.getUserMail()),
            	    safeString(user.getName()),
            	    safeString(user.getSurname()),
            	    safeString(user.getPhoneNumber())
            	};
            backupService.saveBackup(usersList, prefix, fileName, header, userMapper);
        } catch (Exception e) {
            // Handle exception appropriately
            e.printStackTrace();
        }
		
	}
	
	private List<UserDTO> usersToDTO(List<User> list) {
		List<UserDTO> res = new ArrayList<>();
		if(list != null) {
			for(User u : list) {
				res.add(userToDTO(u));
			}
			return res;
		}
		return null;
	}

	private String safeString(Object obj) {
	    return obj != null ? obj.toString() : "";
	}
}
