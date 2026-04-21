package com.example.demo.chat;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Validated
public class MessageDTO {

	@NotNull
	private Integer messageId;

	@NotNull
	@Size(min=1, max=255, message="Message text is too long/empty")
	private String messageText;

	@NotNull
	private Integer chatId;
	
	@NotNull
	private Integer senderId;

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public Integer getChatId() {
		return chatId;
	}

	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	public MessageDTO(Integer messageId, String messageText, Integer chatId, Integer senderId) {
		this.messageId = messageId;
		this.messageText = messageText;
		this.chatId = chatId;
		this.senderId = senderId;
	}
	
	public MessageDTO(String messageText, Integer chatId, Integer senderId) {
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

	public Integer getSenderId() {
		return senderId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}
	
	
	
}
