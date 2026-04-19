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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.user.UserHelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller()
@RequestMapping("chat")
public class ChatController {
	
	@Autowired
	ChatService chatService;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	UserHelper helper;
	
	@GetMapping("redirectToChats")
	public String redirectToChats(@RequestParam(value="delStatus")Optional<String> delStatus, HttpSession session, Model m) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(delStatus.isPresent())
			m.addAttribute("delStatus", delStatus.get());
;		if(userId != null) {
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
	public String createChat(@RequestParam(value="user2Username", required=false)Optional<String> user2Username,
							@RequestParam(value="prodId", required=false)Optional<Integer> prodId,
							RedirectAttributes redirects, HttpSession session) {
//		Integer user1Id = (Integer) session.getAttribute("user1Id");
//		Integer user2Id = (Integer) session.getAttribute("user2Id");
//		if(user1Id == null || user2Id == null)
//			throw new NullPointerException("user1 or 2 is null");
//		if(user1Id == user2Id) {
//			return "redirect:/product/redirectToProductPage?prodId="+prodId.get()+"&chatStatus="+"You cant chat with youself";
//		}
		Integer userId = (Integer) session.getAttribute("userId");
		ChatDTO newChat = null;
		if(userId != null && user2Username.isPresent())
			newChat = new ChatDTO(-1, userId, user2Username.get());
		newChat = chatService.createChat(newChat);
//		return "redirect:redirectToChat?user1Id="+u1Id+"&user2Id="+u2Id;
		if(newChat != null) {
//			session.setAttribute("user1Id", newChat.getUser1Id());
//			session.setAttribute("user2Id", newChat.getUser2Id());
//			return "redirect:redirectToChat?chatId="+newChat.getChatId();
			redirects.addFlashAttribute("chatId", newChat.getChatId());
			return "redirect:redirectToChat";
		}
		else {
			if(prodId.isPresent()) {
//				return "redirect:/product/redirectToProductPage?prodId="+prodId.get()+"&chatStatus="+"Error accured during creating of your chat";
				redirects.addFlashAttribute("prodId", prodId.get());
				redirects.addFlashAttribute("chatStatus", "Error accured during creating of your chat");
				return "redirect:/product/redirectToProductPage";
			}
			return "pages/chats";
		}
			
	}
	
	@GetMapping("redirectToChat")
	public String redirectToChat(@ModelAttribute(value="chatId")Optional<Integer> chatId,
								HttpSession session, HttpServletRequest req, Model m) {
//		Integer userId = (Integer) session.getAttribute("userId");
//		Integer user1Id = (Integer) session.getAttribute("user1Id");
//		Integer user2Id = (Integer) session.getAttribute("user2Id");
		if(chatId.isPresent()) {
			ChatDTO chatDTO = chatService.findById(chatId.get());
			if(chatDTO.getUser1Id() != null && chatDTO.getUser2Id() != null)
				chatDTO = chatService.findByUsers(chatDTO.getUser1Id(), chatDTO.getUser2Id());
			if(chatDTO != null) {
				System.out.println(chatDTO);
				m.addAttribute("messages", chatDTO.getMessages());
				m.addAttribute("chat", chatDTO);
			}
			return "pages/chat";
		}
			
//		return "pages/chats";
		return "redirect:redirectToChats";
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
	public String sendMessage(@ModelAttribute("messageDTO")MessageDTO messageDTO,
							@ModelAttribute("chatDTO")ChatDTO chatDTO,
							RedirectAttributes redirects, Model m) {
		if(chatDTO != null)
			messageDTO.setChatId(chatDTO.getChatId());
		
		MessageDTO newMessage = messageService.saveMessage(messageDTO);
		System.out.println(newMessage);
//		return "redirect:redirectToChat?chatId="+chatDTO.getChatId();
		redirects.addFlashAttribute("chatId", chatDTO.getChatId());
		return "redirect:redirectToChat";
//		return "pages/chat";
	}
	
	@PostMapping("deleteChat")
	public String deleteChat(@RequestParam("chatId")Integer chatId, RedirectAttributes redirects) {
		String delStatus = null;
		try {
			if(!chatService.deleteChat(chatId))
				delStatus = "Error accured during deletion";
			else
				delStatus = "Chat successfully deleted";
		}catch(Exception e) {
			e.printStackTrace();
			delStatus = "Error accured during deletion";
		}
//		return "redirect:redirectToChats?delStatus=" + delStatus;
		redirects.addFlashAttribute("delStatus", delStatus);
		return "redirect:redirectToChats";
	}
}
