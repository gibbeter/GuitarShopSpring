package com.example.demo.chat;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import model.Chat;

public class MessageDTO {

	private int messageId;

	private String messageText;

	private int chatId;
	
	private int senderId;

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public int getChatId() {
		return chatId;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public MessageDTO(int messageId, String messageText, int chatId, int senderId) {
		this.messageId = messageId;
		this.messageText = messageText;
		this.chatId = chatId;
		this.senderId = senderId;
	}
	
	public MessageDTO() {

	}

	@Override
	public String toString() {
		return "MessageDTO [messageId=" + messageId + ", messageText=" + messageText + ", chat=" + chatId + "sender=" + senderId + "]";
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	
	
	
}
