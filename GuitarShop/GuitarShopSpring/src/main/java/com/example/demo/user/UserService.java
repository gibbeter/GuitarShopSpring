package com.example.demo.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.BusinessException;
import com.example.demo.exception.DuplicateEntityException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.InvalidBackUpPathException;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.UserNameTakenException;
import com.example.demo.files.BackUpService;

import model.User;

@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	BackUpService backupService;

    
    public UserDTO userToDTO(User u) {
    	return new UserDTO(u.getUserId(), u.getUserName(), u.getPassword(), u.getType(), u.getUserMail(), u.getName(), u.getSurname(), u.getPhoneNumber());
    }

	public UserDTO saveUser(UserRegDTO user) {
		try {
			Optional<User> existing = userRepo.findUserByUserName(user.getUserName());
			
			if (existing.isPresent()) {
				log.warn("User already exists: {}", user.getUserName());
			    throw new DuplicateEntityException("Username already taken");
			}
			
			User newUser = new User();
			newUser.setUserName(user.getUserName());
			newUser.setPassword(user.getUserPass());
			newUser.setType(user.getType());
			newUser.setUserMail(user.getUserMail());
			newUser.setName(user.getName());
			newUser.setSurname(user.getSurname());
			newUser.setPhoneNumber(user.getPhoneNumber());
			newUser = userRepo.save(newUser);
			
			saveToCSV();
			log.info("New user saved: {}", newUser.getUserName());
			return userToDTO(newUser);
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}
	
	public UserDTO findById(Integer userId) {
		try {
			Optional<User> user = userRepo.findById(userId);
			
			if (!user.isPresent()) {
				log.warn("User not found with id: {}", userId);
			    throw new EntityNotFoundException("User", userId);
			}
			
			
			return userToDTO(user.get());
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}	
	}

	public UserDTO findUserByName(String name) {
		try {
			UserDTO userDTO = new UserDTO();
			Optional<User> user = userRepo.findUserByUserName(name);
			
			if (!user.isPresent()) {
				log.warn("User not found with name: {}", name);
			    throw new EntityNotFoundException("User", name);
			}
			
			userDTO = userToDTO(user.get());

			return userDTO;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	@Transactional
	public boolean updateUserName(Integer userId, String name) {
		try{
			Optional<User> user = userRepo.findById(userId);
			if(user.isEmpty()) {
				log.warn("User not found with id: {}", userId);
				throw new EntityNotFoundException("User", userId);
			}
				
			if(!user.get().getUserName().equalsIgnoreCase(name)) {
				if(userRepo.findUserByUserName(name).isPresent())
					throw new UserNameTakenException(user.get().getUserName());
				user.get().setUserName(name);
				userRepo.save(user.get());
				return true;
			}

			return false;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	@Transactional
	public boolean updatePass(Integer userId, String pass) {
		try{
			Optional<User> user = userRepo.findById(userId);
			if(user.isEmpty()) {
				log.warn("User not found with id: {}", userId);
				throw new EntityNotFoundException("User", userId);
			}
			
			
			if(pass.length() < 6) {
				log.warn("Password is too short (min 6 symbols): {}", pass);
				throw new InvalidPasswordException("Password is too short (min 6 symbols)");
			}
			
			if(user.get().getPassword().equals(pass)) {
				log.info("Same password: {}", pass);
				return false;
			}
			
			user.get().setPassword(pass);
			userRepo.save(user.get());
			return true;

		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}
	
	@Transactional
	public boolean updateMail(Integer userId, String mail) {
		try{
			Optional<User> user = userRepo.findById(userId);
			if(user.isEmpty()) {
				log.warn("User not found with id: {}", userId);
				throw new EntityNotFoundException("User", userId);
			}
			
			if(!user.get().getUserMail().equals(mail)) {
				user.get().setUserMail(mail);
				userRepo.save(user.get());
				return true;
			}
			return false;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}
	
	@Transactional
	public boolean updateName(Integer userId, String name) {
		try{
			Optional<User> user = userRepo.findById(userId);
			if(user.isEmpty()) {
				log.warn("User not found with id: {}", userId);
				throw new EntityNotFoundException("User", userId);
			}
			
			if(!user.get().getName().equals(name)) {
				user.get().setName(name);
				userRepo.save(user.get());
				return true;
			}
			return false;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	@Transactional
	public boolean updateSurname(Integer userId, String surname) {
		try{
			Optional<User> user = userRepo.findById(userId);
			if(user.isEmpty()) {
				log.warn("User not found with id: {}", userId);
				throw new EntityNotFoundException("User", userId);
			}
			
			if(!user.get().getSurname().equals(surname)) {
				user.get().setSurname(surname);
				userRepo.save(user.get());
				return true;
			}
			return false;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	@Transactional
	public boolean updatePhoneNumber(Integer userId, Integer phoneNumber) {
		try{
			Optional<User> user = userRepo.findById(userId);
			if(user.isEmpty()) {
				log.warn("User not found with id: {}", userId);
				throw new EntityNotFoundException("User", userId);
			}
			
			if(!user.get().getPhoneNumber().equals(phoneNumber)) {
				user.get().setPhoneNumber(phoneNumber);
				return userRepo.save(user.get()) != null;
			}
			return false;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
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
			return userToDTO(u);
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	public void removeUser(int userId) {
		try {
			Optional<User> user = userRepo.findById(userId);
			if(user.isEmpty()) {
				log.warn("User not found with id: {}", userId);
				throw new EntityNotFoundException("User", userId);
			}
			
			userRepo.delete(user.get());
			saveToCSV();
			
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
		
	}

	public String getUserNameAndMail(Integer userId) {
		try {
			Optional<User> user = userRepo.findById(userId);
			
			if(user.isEmpty()) {
				log.warn("User not found with id: {}", userId);
				throw new EntityNotFoundException("User", userId);
			}
			
			return user.get().getUserMail()+":"+user.get().getUserName();
			
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
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
            
        }catch(InvalidBackUpPathException e) {
			log.error("Backup error: {}", e.getMessage());
		}
		
	}
	
	private List<UserDTO> usersToDTO(List<User> list) {
		List<UserDTO> res = new ArrayList<>();
		if(list != null) {
			for(User u : list) {
				res.add(userToDTO(u));
			}
		}
		return res;
	}

	private String safeString(Object obj) {
	    return obj != null ? obj.toString() : "";
	}
	
}
