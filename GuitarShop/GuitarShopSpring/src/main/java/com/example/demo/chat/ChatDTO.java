package com.example.demo.chat;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import model.Message;

@Validated
public class ChatDTO {

	@NotNull
	private Integer chatId;
	
	@NotNull
	private Integer user1Id;

	@NotNull
	private Integer user2Id;
	
	@NotNull
	@Size(min=1, max=255, message="User name is too long/Cant be empty")
	private String user1Username;
	
	@NotNull
	@Size(min=1, max=255, message="User name is too long/Cant be empty")
	private String user2Username;	
	
	public String getUser1Username() {
		return user1Username;
	}

	public void setUser1Username(String user1Username) {
		this.user1Username = user1Username;
	}

	public String getUser2Username() {
		return user2Username;
	}

	public void setUser2Username(String user2Username) {
		this.user2Username = user2Username;
	}

	private List<MessageDTO> messages;

	public Integer getChatId() {
		return chatId;
	}

	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	public Integer getUser1Id() {
		return user1Id;
	}

	public void setUser1Id(Integer user1Id) {
		this.user1Id = user1Id;
	}

	public Integer getUser2Id() {
		return user2Id;
	}

	public void setUser2Id(Integer user2Id) {
		this.user2Id = user2Id;
	}

	public List<MessageDTO> getMessages() {
		return messages;
	}

	public void setMessages(List<MessageDTO> messages) {
		this.messages = messages;
	}

	
	
	public ChatDTO(Integer chatId, Integer user1Id, Integer user2Id, String user1Username, String user2Username,
			List<MessageDTO> messages) {
		this.chatId = chatId;
		this.user1Id = user1Id;
		this.user2Id = user2Id;
		this.user1Username = user1Username;
		this.user2Username = user2Username;
		this.messages = messages;
	}

	public ChatDTO(Integer chatId, Integer user1Id, Integer user2Id) {
		this.chatId = chatId;
		this.user1Id = user1Id;
		this.user2Id = user2Id;
	}
	
	public ChatDTO(Integer chatId, Integer user1Id, String user2Username) {
		this.chatId = chatId;
		this.user1Id = user1Id;
		this.user2Username = user2Username;
	}

	public ChatDTO() {
	}

	@Override
	public String toString() {
		return "ChatDTO [chatId=" + chatId + ", user1Id=" + user1Id + ", user2Id=" + user2Id + ", user1Username="
				+ user1Username + ", user2Username=" + user2Username + ", messages=" + messages + "]";
	}

	
}
