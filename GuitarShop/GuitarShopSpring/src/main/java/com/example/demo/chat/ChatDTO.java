package com.example.demo.chat;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import model.Message;

public class ChatDTO {

	private int chatId;

	private int user1Id;

	private int user2Id;

	private List<MessageDTO> messages;

	public int getChatId() {
		return chatId;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public int getUser1Id() {
		return user1Id;
	}

	public void setUser1Id(int user1Id) {
		this.user1Id = user1Id;
	}

	public int getUser2Id() {
		return user2Id;
	}

	public void setUser2Id(int user2Id) {
		this.user2Id = user2Id;
	}

	public List<MessageDTO> getMessages() {
		return messages;
	}

	public void setMessages(List<MessageDTO> messages) {
		this.messages = messages;
	}

	public ChatDTO(int chatId, int user1Id, int user2Id, List<MessageDTO> messages) {
		this.chatId = chatId;
		this.user1Id = user1Id;
		this.user2Id = user2Id;
		this.messages = messages;
	}
	
	public ChatDTO(int chatId, int user1Id, int user2Id) {
		this.chatId = chatId;
		this.user1Id = user1Id;
		this.user2Id = user2Id;
	}

	public ChatDTO() {
	}

	@Override
	public String toString() {
		return "ChatDTO [chatId=" + chatId + ", user1Id=" + user1Id + ", user2Id=" + user2Id + ", messages=" + messages
				+ "]";
	}
	
	
	
}
