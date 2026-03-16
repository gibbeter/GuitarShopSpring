package com.example.demo.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller()
@RequestMapping("chat")
public class ChatController {
	
	@Autowired
	ChatService chatService;
	
	@Autowired
	MessageService messageService;
	
	@GetMapping("redirectToChats")
	public String redirectToChats(@RequestParam(value="userId", required=false)Optional<String> userId, Model m) {
		if(userId.isPresent()) {
			List<ChatDTO> chats = chatService.findAllByUser(userId);
			for(ChatDTO c: chats) {
				System.out.println(c);
			}
			m.addAttribute("chats", chats);
			return "pages/chats";
		}
		return "pages/account";
	}
	
	@GetMapping("createChat")
	public String createChat(@RequestParam(value="user1Id")Integer u1Id, @RequestParam(value="user2Id")Integer u2Id) {
		ChatDTO newChat = new ChatDTO(-1, u1Id, u2Id);
		newChat = chatService.createChat(newChat);
//		return "redirect:redirectToChat?user1Id="+u1Id+"&user2Id="+u2Id;
		if(newChat != null)
			return "redirect:redirectToChat?user1Id="+u1Id+"&user2Id="+u2Id;
		else
			return "pages/account";
	}
	
	@GetMapping("redirectToChat")
	public String redirectToChat(@RequestParam(value="user1Id")Integer u1Id, @RequestParam(value="user2Id")Integer u2Id, Model m) {
		ChatDTO chat = chatService.findByUsers(u1Id, u2Id);
		if(chat != null) {
			System.out.println(chat);
			m.addAttribute("messages", chat.getMessages());
			m.addAttribute("chat", chat);
		}
			
		return "pages/chat";
//		return "redirect:redirectToChat?user1Id="+u1Id+"&user2Id="+u2Id;
	}
	
	@ModelAttribute("messageDTO")
	public MessageDTO getMessageDTO() {
		return new MessageDTO();
	}
	
	@ModelAttribute("chatDTO")
	public ChatDTO getChatDTO() {
		return new ChatDTO();
	}
	
	@PostMapping("sendMessage")
	public String sendMessage(@ModelAttribute("messageDTO")MessageDTO messageDTO, @ModelAttribute("chatDTO")ChatDTO chatDTO, @RequestParam(value="user1Id")Integer u1Id, @RequestParam(value="user2Id")Integer u2Id, Model m) {
		if(chatDTO != null)
			messageDTO.setChatId(chatDTO.getChatId());
		MessageDTO newMessage = messageService.saveMessage(messageDTO);
		System.out.println(newMessage);
		return "redirect:redirectToChat?user1Id="+u1Id+"&user2Id="+u2Id;
//		return "pages/chat";
	}
}
