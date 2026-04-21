package com.example.demo.chat;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.DuplicateEntityException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.user.UserHelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller()
@RequestMapping("chat")
public class ChatController {
	
	@Autowired
	ChatService chatService;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	UserHelper helper;
	
	private static final Logger log = LoggerFactory.getLogger(ChatController.class);

	
	@GetMapping("redirectToChats")
	public String redirectToChats(@ModelAttribute(value="delStatus")Optional<String> delStatus, HttpSession session, Model m) {
		Integer userId = getAuthUserId(session);
		List<ChatDTO> chats = chatService.findAllByUser(userId);
		m.addAttribute("chats", chats);
		return "pages/chats";
	}
	
	@GetMapping("createChat")
	public String createChat(@RequestParam(value="user2Username")Optional<String> user2Username,
							@RequestParam(value="prodId")Optional<Integer> prodId,
							RedirectAttributes redirects, HttpSession session) {

		Integer userId = getAuthUserId(session);
		try {
			ChatDTO newChat = chatService.createChat(userId, user2Username);
			redirects.addFlashAttribute("chatId", newChat.getChatId());
			return "redirect:redirectToChat";
		}catch(EntityNotFoundException e) {
			redirects.addFlashAttribute("chatStatus", "User not found");
		}
		catch(DuplicateEntityException e) {
			ChatDTO newChat = chatService.findByUsers(userId, user2Username.get());
			return "redirect:redirectToChat?chatId="+newChat.getChatId();
		}
		catch(AccessDeniedException e) {
			redirects.addFlashAttribute("chatStatus", e.getMessage());
		}
		catch(BusinessException e) {
			redirects.addFlashAttribute("chatStatus", "Internal error accured during creating of your chat");
		}
		return "redirect:/product/redirectToProductPage?prodId="+prodId.get();	
	}
	
	@GetMapping("redirectToChat")
	public String redirectToChat(@RequestParam(value="chatId")Optional<Integer> chatId,
								HttpSession session, HttpServletRequest req, RedirectAttributes redirects, Model m) {
		if(chatId.isPresent()) {
			try {
			Integer userId = getAuthUserId(session);
			ChatDTO chatDTO = chatService.accessChat(chatId.get(), userId);
			
			m.addAttribute("messages", chatDTO.getMessages());
			m.addAttribute("chat", chatDTO);
			
			return "pages/chat";
			}catch(AccessDeniedException e) {
				redirects.addFlashAttribute("delStatus", "Acess to this chat denied");
				return "redirect:redirectToChats";
			}
		}
		return "redirect:redirectToChats";
	}
	
	@ModelAttribute("messageDTO")
	public MessageDTO getMessageDTO() {
		return new MessageDTO();
	}
	
	@ModelAttribute("createMessageDTO")
	public CreateMessageDTO getCreateMessageDTO() {
		return new CreateMessageDTO();
	}
	
	@ModelAttribute("chatDTO")
	public ChatDTO getChatDTO() {
		return new ChatDTO();
	}
	
	@PostMapping("sendMessage")
	public String sendMessage(@Valid @ModelAttribute("createMessageDTO")CreateMessageDTO createMessageDTO,
							RedirectAttributes redirects, Model m) {

		MessageDTO messageDTO = new MessageDTO(
				createMessageDTO.getMessageText(),
				createMessageDTO.getChatId(),
				createMessageDTO.getSenderId()
			);

		messageService.saveMessage(messageDTO);
		return "redirect:redirectToChat?chatId=" + createMessageDTO.getChatId();
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

		redirects.addFlashAttribute("delStatus", delStatus);
		return "redirect:redirectToChats";
	}
	
	private Integer getAuthUserId(HttpSession session) {
	    Integer userId = (Integer) session.getAttribute("userId");
	    if(userId == null) {
	        log.warn("Unauthorized access attempt");
	        throw new AccessDeniedException("You must be logged in");
	    }
	    return userId;
	}
}
