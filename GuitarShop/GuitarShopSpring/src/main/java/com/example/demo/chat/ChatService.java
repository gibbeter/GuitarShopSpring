package com.example.demo.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.DuplicateEntityException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.user.UserDTO;
import com.example.demo.user.UserRepo;
import com.example.demo.user.UserService;

import model.Chat;
import model.Message;
import model.User;

@Service
public class ChatService {
	
	@Autowired
	ChatRepo chatRepo;
	
	@Autowired
	MessageRepo messageRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	UserService userService;
	
	private static final Logger log = LoggerFactory.getLogger(ChatService.class);

	public List<ChatDTO> findAllByUser(Integer userId) {
		List<ChatDTO> res = new ArrayList<>();
		try {
			List<Chat> list = chatRepo.findAllByUser(userId);
			for(Chat c : list) {
				res.add(chatToDTO(c));
			}
			return res;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}
	
	private List<MessageDTO> messagesToDTO(List<Message> messages) {
		List<MessageDTO> res = new ArrayList<>();
		for(Message m: messages) {
			res.add(new MessageDTO(m.getMessageId(), m.getMessageText(), m.getChat().getChatId(), m.getSenderId()));
		}
		return res;
	}
	
	private ChatDTO chatToDTO(Chat c) {
		return new ChatDTO(c.getChatId(), c.getUser1Id(), c.getUser2Id(), c.getUser1Username(), c.getUser2Username(), messagesToDTO(c.getMessages()));
	}

	public ChatDTO findByUsers(Integer u1ID, Integer u2ID) {
		try {
			Optional<Chat> chat = chatRepo.findByUsers(u1ID, u2ID);
			if(chat.isEmpty()) {
				log.warn("Entity not found with id: {}", "user1" + u1ID + ": user2" + u2ID);
				throw new EntityNotFoundException("Chat", "user1" + u1ID + ": user2" + u2ID);
			}
			
			ChatDTO chatDTO = chatToDTO(chat.get());
			return chatDTO;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
		
	}
	
	public ChatDTO findByUsers(Integer u1ID, String u2Name) {
		try {
			UserDTO u2 = userService.findUserByName(u2Name);
			Integer u2ID = u2.getUserId();
			Optional<Chat> chat = chatRepo.findByUsers(u1ID, u2ID);
			if(chat.isEmpty()) {
				log.warn("Entity not found with id: {}", "user1" + u1ID + ": user2" + u2ID);
				throw new EntityNotFoundException("Chat", "user1" + u1ID + ": user2" + u2ID);
			}
			
			ChatDTO chatDTO = chatToDTO(chat.get());
			return chatDTO;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
		
	}

	public ChatDTO findById(Integer chatId) {
		Optional<Chat> chat = chatRepo.findById(chatId);
		if(chat.isEmpty()) {
			log.warn("Entity not found with id: {}", chatId);
			throw new EntityNotFoundException("Chat", chatId);
		}
		
		return chatToDTO(chat.get());
	}

	public boolean deleteChat(Integer chatId) {
		try {
			Optional<Chat> chat = chatRepo.findById(chatId);
			if(chat.isEmpty()) {
				log.warn("Entity not found with id: {}", chatId);
				throw new EntityNotFoundException("Chat", chatId);
			}
			
			List<Message> list = chat.get().getMessages();
			for(Message m : list) {
				messageRepo.delete(m);
			}
			chatRepo.delete(chat.get());
			return true;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
		
	}

	public ChatDTO createChat(Integer userId, Optional<String> user2Username) {
		if(user2Username.isEmpty())
			throw new BusinessException("IVALID_PARAM", "Invalid parameter user2Username on creation");
		UserDTO u1Existing = userService.findById(userId);
		UserDTO u2Existing = userService.findUserByName(user2Username.get());
		if(u1Existing.getUserId().equals(u2Existing.getUserId())) {
			log.warn("Cant chat with youself: {}", "user1" + u1Existing.getUserId() + ": user2" + u2Existing.getUserId());
			throw new AccessDeniedException("You cant create a chat with yourself");
		}
		Optional<Chat> chat = chatRepo.findByUsers(u1Existing.getUserId(), u2Existing.getUserId());
		if(chat.isPresent()) {
			log.warn("Duplicate entity with id: {}", "user1" + u1Existing.getUserId() + ": user2" + u2Existing.getUserId());
			throw new DuplicateEntityException("Duplicate chat entity, chat already exists");
		}
		
		try {
			Chat c = new Chat();
			c.setUser1Id(u1Existing.getUserId());
			c.setUser2Id(u2Existing.getUserId());
			c.setUser1Username(u1Existing.getUserName());
			c.setUser2Username(u2Existing.getUserName());
			c = chatRepo.save(c);
			return chatToDTO(c);
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	public ChatDTO accessChat(Integer chatId, Integer userId) {
		ChatDTO chatDTO = findById(chatId);
		if(userId.equals(chatDTO.getUser1Id()) || userId.equals(chatDTO.getUser2Id()))
			return chatDTO;
		else {
			log.warn("Access to chat denied");
			throw new AccessDeniedException("You cant access this chat");
		}
	}
	
}
