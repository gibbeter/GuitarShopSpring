package com.example.demo.chat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Chat;
import model.Message;

@Service
public class MessageService {
	
	@Autowired
	MessageRepo messageRepo;
	
	@Autowired
	ChatRepo chatRepo;

	public MessageDTO saveMessage(MessageDTO messageDTO) {
		try {
			Message m = new Message();
			Chat chat = chatRepo.findById(messageDTO.getChatId()).get();
			m.setChat(chat);
			m.setMessageText(messageDTO.getMessageText());
			m.setSenderId(messageDTO.getSenderId());
			
			chat.getMessages().add(m);
			
			return messageToDTO(messageRepo.save(m));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
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
