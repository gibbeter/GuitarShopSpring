package com.example.demo.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.demo.cartitem.CartDTO;
import com.example.demo.cartitem.CartService;
import com.example.demo.cartitem.ItemService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import model.User;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	UserHelper helper;
	
	@GetMapping("redirectToIndex")
	public String redirectToIdx(HttpServletRequest req, HttpSession session) {
		
		Integer userId = (Integer) session.getAttribute("userId");

		if(userId != null) {
			getUserData(userId, session, req);
		}
		
		req.getSession().removeAttribute("updatePassStatus");
		req.getSession().removeAttribute("updateMailStatus");
		req.getSession().removeAttribute("updateUserNStatus");
		req.getSession().removeAttribute("updateNameStatus");
		req.getSession().removeAttribute("updateSurnameStatus");
		req.getSession().removeAttribute("updatePhoneNumberStatus");
		return "index";
	}
	
//	@GetMapping("redirectToAccount")
//	public String redirectToAcc(@RequestParam(value="activeUser", required = false)Optional<Integer> userId, HttpServletRequest req) {
//		if(userId.isPresent()) {
//			getUserData(userId.get(), req);
//		}
//		return "pages/account";
//	}
	
	@GetMapping("redirectToAccount")
	public String redirectToAcc(HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		
		res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    res.setHeader("Pragma", "no-cache");
	    res.setDateHeader("Expires", 0);
		
		Integer userId = (Integer) session.getAttribute("userId");

		if(userId != null) {
			getUserData(userId, session, req);
		}
//		if(session.getAttribute("errorStatus") != null)
//			session.removeAttribute("errorStatus");
		if(req.getSession().getAttribute("regStatus") != null)
			req.getSession().removeAttribute("regStatus");
		return "pages/account";
	}
	
	@ModelAttribute("userDTO")
	public UserDTO getUserDTO() {
		return new UserDTO();
	}
	
	@ModelAttribute("userRegDTO")
	public UserRegDTO getUserRegDTO() {
		return new UserRegDTO();
	}
	
	@PostMapping("login")
	public String loginUser(@RequestParam("userName")String userN, @RequestParam("userPass")String userP, HttpSession session, HttpServletRequest req, Model m) {
		if(userN.contains("guest")) {
			req.getSession().setAttribute("errorStatus", "Username cant contain \"guest\"");
			return "pages/account";
		}
		UserDTO user = userService.findUserByName(userN);
		if(user != null) {
			if(user.getUserPass().contentEquals(userP)) {
				getUserData(user.getUserId(), session, req);
//				session.setAttribute("userId", user.getUserId());
//				session.setAttribute("userName", user.getUserName());
			}
			else {
				req.getSession().setAttribute("errorStatus", "Wrong password");
			}
		}
		else {
			req.getSession().setAttribute("errorStatus", "User with this login doesnt exist");
		}
		
		return "redirect:redirectToAccount";
	}
	
	@PostMapping("register")
	public String registerUser(@Valid @ModelAttribute("userRegDTO")UserRegDTO userReg, BindingResult bindingResult, HttpServletRequest req, Model m, HttpServletResponse res) {
//		String currentPage = req.getRequestURI();
//		return "/GuitarShop/";
//		 try {
//			 if(!userService.saveUser(user))
//					m.addAttribute("status", "Error during registration");
//	            return ResponseEntity.ok("User saved");
//	        } catch (ValidationException e) {
//	            return ResponseEntity
//	                .badRequest()
//	                .body(e.getMessage());
//	        } catch (Exception e) {
//	            return ResponseEntity
//	                .status(500)
//	                .body("Internal server error");
//	        }
		if(bindingResult.hasErrors()) {
			m.addAttribute("errorStatus", "Validation error during registration, check if you filled all fields");
			System.out.println(bindingResult.getAllErrors());
			return "pages/account";
		}
		else if(userReg.getUserName().contains("guest")) {
			m.addAttribute("errorStatus", "Username cant contain \"guest\"");
			System.out.println(bindingResult.getAllErrors());
			return "pages/account";
		}
		else {
			if(req.getAttribute("errorStatus") != null)
				req.removeAttribute("errorStatus");
			try {
				if(!userService.saveUser(userReg)) {
					m.addAttribute("regStatus", "Error during registration, try using different username");
//					res.sendRedirect("redirectToAccount");
					return "pages/account";
				}
				else {
					m.addAttribute("errorStatus", "New user has been registered");
//					req.getSession().setAttribute("userName", user.getUserName());
					return "pages/account";
//					res.sendRedirect("redirectToIndex");
				}
					
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		
		
//		try {
//			res.sendRedirect("redirectToAccount");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return "redirect:redirectToAccount";
	}
	
	public void getUserData(Integer userId, HttpSession session, HttpServletRequest req) {
		System.out.println("GET DATA");
		UserDTO user = userService.findById(userId);
		if(user != null) {
			session.setAttribute("userId", user.getUserId());
			session.setAttribute("userName",  user.getUserName());
			session.setAttribute("userMail",  user.getUserMail());
			session.setAttribute("userPassword",  user.getUserPass());
			session.setAttribute("userType",  user.getType());
			session.setAttribute("name",  user.getName());
			session.setAttribute("surname",  user.getSurname());
			session.setAttribute("phoneNumber",  user.getPhoneNumber());
		}
	}
	
	@GetMapping("logout")
	public String userLogout(HttpSession session, HttpServletRequest req) {
		Integer id = (Integer) req.getSession().getAttribute("userId");
		if(id != null) {
			UserDTO u = userService.findById(id);
			if(u != null && u.getType().equalsIgnoreCase("guest")) {
				try {
					CartDTO cartDTO = cartService.findCartByUser(id);
					itemService.removeAllCartItems(cartDTO.getCartId());
			    	cartService.removeCart(cartDTO.getCartId());
				}catch(Exception e) {
					e.printStackTrace();
				}
//		    	userService.removeUser(u.getUserId());
		    }
		}
		session.removeAttribute("userId");
		session.removeAttribute("userName");
		session.removeAttribute("userMail");
		session.removeAttribute("userPassword");
		session.removeAttribute("userType");
		
		req.getSession().removeAttribute("updatePassStatus");
		req.getSession().removeAttribute("updateMailStatus");
		req.getSession().removeAttribute("updateUserNStatus");
		
		return "redirect:redirectToAccount";
	}
	
	@PostMapping("changeUserName")
	public String changeUserName(@RequestParam("userName")String name, HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(!userService.updateUserName(userId, name)) {
			req.getSession().setAttribute("updateUserNStatus", "Error during update operation");
		}
		else {
			req.getSession().setAttribute("updateUserNStatus", "Successfull update!");
		}
		
		getUserData(userId, session, req);
		statusManager("userN", session, req);
		
		return "redirect:redirectToAccount";
	}
	
	@PostMapping("changePass")
	public String changePass(@RequestParam("userPass")String pass, HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(!userService.updatePass(userId, pass)) {
			req.getSession().setAttribute("updatePassStatus", "Error during update operation");
		}
		else {
			req.getSession().setAttribute("updatePassStatus", "Successfull update!");
		}
		
		getUserData(userId, session, req);
		statusManager("pass", session, req);

		return "redirect:redirectToAccount";
	}
	
	@PostMapping("changeMail")
	public String changeMail(@RequestParam("userMail")String mail, HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(!userService.updateMail(userId, mail)) {
			req.getSession().setAttribute("updateMailStatus", "Error during update operation");
		}
		else {
			req.getSession().setAttribute("updateMailStatus", "Successfull update!");
		}
		
		getUserData(userId, session, req);
		statusManager("mail",session, req);

		return "redirect:redirectToAccount";
	}
	
	@PostMapping("changeName")
	public String changeName(@RequestParam("name")String name, HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(!userService.updateName(userId, name)) {
			req.getSession().setAttribute("updateNameStatus", "Error during update operation");
		}
		else {
			req.getSession().setAttribute("updateNameStatus", "Successfull update!");
		}
		
		getUserData(userId, session, req);
		statusManager("name",session, req);

		return "redirect:redirectToAccount";
	}
	
	@PostMapping("changeSurname")
	public String changeSurname(@RequestParam("surname")String surname, HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(!userService.updateSurname(userId, surname)) {
			req.getSession().setAttribute("updateSurnameStatus", "Error during update operation");
		}
		else {
			req.getSession().setAttribute("updateSurnameStatus", "Successfull update!");
		}
		
		getUserData(userId, session, req);
		statusManager("surname",session, req);

		return "redirect:redirectToAccount";
	}
	
	@PostMapping("changePhoneNumber")
	public String changePhoneNumber(@RequestParam("phoneNumber")Integer phoneNumber, HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		
		Integer userId = (Integer) session.getAttribute("userId");
		if(!userService.updatePhoneNumber(userId, phoneNumber)) {
			req.getSession().setAttribute("updatePhoneNumberStatus", "Error during update operation");
		}
		else {
			req.getSession().setAttribute("updatePhoneNumberStatus", "Successfull update!");
		}
		
		getUserData(userId, session, req);
		statusManager("phoneNumber",session, req);

		return "redirect:redirectToAccount";
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String error = String.format("Parameter " + ex.getName() + " has invalid value " + ex.getValue() + " - expected type " + ex.getRequiredType().getSimpleName());
//        return ResponseEntity.badRequest().body(error);
        if(ex.getName().equalsIgnoreCase("phonenumber"))
        	req.getSession().setAttribute("updatePhoneNumberStatus", "Number is too long");
        return "redirect:redirectToAccount";
    }
	
	
	
	public void statusManager(String type, HttpSession session, HttpServletRequest req) {
		
		ArrayList<String> fields = new ArrayList<>(Arrays.asList("UserN", "Pass", "Mail", "Name", "Surname", "PhoneNumber"));
		
		for(String t : fields) {
			if(!t.equalsIgnoreCase(type)) {
				session.removeAttribute(new StringBuilder("update")
					    .append(t)
					    .append("Status")
					    .toString());
			}
		}
		
//		if(type.equalsIgnoreCase("userN")) {
//			session.removeAttribute("updatePassStatus");
//			session.removeAttribute("updateMailStatus");
//		}
	}
	
	public UserDTO createGuest() {
		try {
			UserDTO guest = userService.createGuestUser();
			return guest;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
