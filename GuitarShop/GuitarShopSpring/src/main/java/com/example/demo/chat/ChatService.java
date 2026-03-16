package com.example.demo.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Chat;
import model.Message;

@Service
public class ChatService {
	
	@Autowired
	ChatRepo chatRepo;

	public List<ChatDTO> findAllByUser(Optional<String> userId) {
		List<ChatDTO> res = new ArrayList<>();
		try {
			List<Chat> list = chatRepo.findAllByUser(userId);
			for(Chat c : list) {
				res.add(new ChatDTO(c.getChatId(), c.getUser1Id(), c.getUser2Id(), messagesToDTO(c.getMessages())));
			}
			return res;
		}catch(Exception e) {
			return res;
		}
	}

	public ChatDTO createChat(ChatDTO newChat) {
		try {
			Chat chat = chatRepo.findByUsers(newChat.getUser1Id(), newChat.getUser2Id());
			ChatDTO chatDTO = chatToDTO(chat);
			if(chatDTO == null) {
				Chat c = new Chat();
				c.setUser1Id(newChat.getUser1Id());
				c.setUser2Id(newChat.getUser2Id());
				c = chatRepo.save(c);
				return new ChatDTO(c.getChatId(), c.getUser1Id(), c.getUser2Id(), messagesToDTO(c.getMessages()));
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
		return new ChatDTO(c.getChatId(), c.getUser1Id(), c.getUser2Id(), messagesToDTO(c.getMessages()));
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

}
