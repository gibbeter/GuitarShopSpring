package model;

import java.io.Serializable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the chat database table.
 * 
 */
@Entity
@NamedQuery(name="Chat.findAll", query="SELECT c FROM Chat c")
public class Chat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="chat_id")
	private int chatId;

	@Column(name="user1_id")
	private int user1Id;

	@Column(name="user2_id")
	private int user2Id;
	
	@Column(name="user1_username")
	private String user1Username;

	@Column(name="user2_username")
	private String user2Username;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="chat")
	private List<Message> messages = new ArrayList<>();

	public Chat() {
	}

	public int getChatId() {
		return this.chatId;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public int getUser1Id() {
		return this.user1Id;
	}

	public void setUser1Id(int user1Id) {
		this.user1Id = user1Id;
	}

	public int getUser2Id() {
		return this.user2Id;
	}

	public void setUser2Id(int user2Id) {
		this.user2Id = user2Id;
	}

	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	
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

	public Message addMessage(Message message) {
		getMessages().add(message);
		message.setChat(this);

		return message;
	}

	public Message removeMessage(Message message) {
		getMessages().remove(message);
		message.setChat(null);

		return message;
	}

}