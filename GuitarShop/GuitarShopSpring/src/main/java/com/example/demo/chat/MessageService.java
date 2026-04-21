package com.example.demo.chat;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.exception.BusinessException;
import com.example.demo.exception.EntityNotFoundException;

import model.Chat;
import model.Message;

@Service
public class MessageService {
	
	@Autowired
	MessageRepo messageRepo;
	
	@Autowired
	ChatRepo chatRepo;
	
	private static final Logger log = LoggerFactory.getLogger(MessageService.class);

	public MessageDTO saveMessage(MessageDTO messageDTO) {
		try {
			Message m = new Message();
			Optional<Chat> chat = chatRepo.findById(messageDTO.getChatId());
			if(chat.isEmpty()) {
				log.warn("Entity not found with id: {}", messageDTO.getChatId());
				throw new EntityNotFoundException("Chat", messageDTO.getChatId());
			}
			Chat c = chat.get();
			
			m.setChat(c);
			m.setMessageText(messageDTO.getMessageText());
			m.setSenderId(messageDTO.getSenderId());
			
			c.getMessages().add(m);
//			chatRepo.save(c);
			m = messageRepo.save(m);
			return messageToDTO(m);
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}
	
	private MessageDTO messageToDTO(Message m) {
		return new MessageDTO(m.getMessageId(), m.getMessageText(), m.getChat().getChatId(), m.getSenderId());
	}
	
//	private ChatDTO chatToDTO(Chat c) {
//		return new ChatDTO(c.getChatId(), c.getUser1Id(), c.getUser2Id(), messagesToDTO(c.getMessages()));
//	}
	
//	private List<MessageDTO> messagesToDTO(List<Message> messages) {
//		List<MessageDTO> res = new ArrayList<>();
//		for(Message m: messages) {
//			res.add(new MessageDTO(m.getMessageId(), m.getMessageText(), m.getChat().getChatId(), m.getSenderId()));
//		}
//		return null;
//	}
	
}
