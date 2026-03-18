package com.example.demo.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.user.UserDTO;
import com.example.demo.user.UserRepo;

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

	public List<ChatDTO> findAllByUser(Integer userId) {
		List<ChatDTO> res = new ArrayList<>();
		try {
			List<Chat> list = chatRepo.findAllByUser(userId);
			for(Chat c : list) {
				res.add(chatToDTO(c));
			}
			return res;
		}catch(Exception e) {
			return res;
		}
	}

	public ChatDTO createChat(ChatDTO newChat) {
		try {
			User user1 = userRepo.findById(newChat.getUser1Id()).get();
			User user2 = userRepo.findUserByUserName(newChat.getUser2Username());
			Chat chat = chatRepo.findByUsers(newChat.getUser1Id(), user2.getUserId());
			ChatDTO chatDTO = chatToDTO(chat);
			if(chatDTO == null) {
				Chat c = new Chat();
				c.setUser1Id(user1.getUserId());
				c.setUser2Id(user2.getUserId());
				c.setUser1Username(user1.getUserName());
				c.setUser2Username(user2.getUserName());
				c = chatRepo.save(c);
				return chatToDTO(c);
			}
			return chatDTO;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
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
		try {
			return new ChatDTO(c.getChatId(), c.getUser1Id(), c.getUser2Id(), c.getUser1Username(), c.getUser2Username(), messagesToDTO(c.getMessages()));
		}catch(Exception e) {
			return null;
		}
		
	}

	public ChatDTO findByUsers(Integer u1Id, Integer u2Id) {
		try {
			Chat chat = chatRepo.findByUsers(u1Id, u2Id);
			ChatDTO chatDTO = chatToDTO(chat);
			return chatDTO;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public ChatDTO findById(Integer chatId) {
		try {
			return chatToDTO(chatRepo.findById(chatId).get());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean deleteChat(Integer chatId) {
		try {
			Chat chat = chatRepo.findById(chatId).get();
			List<Message> list = chat.getMessages();
			for(Message m : list) {
				messageRepo.delete(m);
			}
			chatRepo.delete(chat);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

}
