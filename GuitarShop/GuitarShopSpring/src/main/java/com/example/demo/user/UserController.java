package com.example.demo.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.CustomUserDetailsService;
import com.example.demo.cartitem.CartDTO;
import com.example.demo.cartitem.CartService;
import com.example.demo.cartitem.ItemService;
import com.example.demo.exception.DuplicateEntityException;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.UserNameTakenException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

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
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private HttpSessionSecurityContextRepository securityContextRepository;
	
	@Value("${app.message.update-error}")
	private String updateMessageErr;
	
	@Value("${app.message.update-succes}")
	private String updateMessageSucc;
	
	@GetMapping("redirectToIndex")
	public String redirectToIdx(HttpServletRequest req, HttpSession session) {
		
		Integer userId = (Integer) session.getAttribute("userId");

		if(userId != null) {
			getUserData(userId, session);
		}

		return "index";
	}

	
	@GetMapping("redirectToAccount")
	public String redirectToAcc(HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		
		res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    res.setHeader("Pragma", "no-cache");
	    res.setDateHeader("Expires", 0);
		
		Integer userId = (Integer) session.getAttribute("userId");

		if(userId != null) {
			getUserData(userId, session);
		}
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
	public String loginUser(@RequestParam("userName")String userN,
							@RequestParam("userPass")String userP,
							HttpSession session, HttpServletRequest req, HttpServletResponse res,
							RedirectAttributes redirects, Model m) {
		
		UserDetails userDetails;
	    try {
	        userDetails = customUserDetailsService.loadUserByUsername(userN);
	    } catch (UsernameNotFoundException e) {
	        redirects.addFlashAttribute("loginStatus", "User with this login doesn't exist");
	        return "redirect:redirectToAccount";
	    }
		
		if(userN.contains("guest")) {
			redirects.addFlashAttribute("loginStatus",  "Username cant contain \"guest\"");
			return "redirect:redirectToAccount";
		}
		
		UserDTO user = userService.findUserByName(userN);
		if(user != null) {
			if (passwordEncoder.matches(userP, user.getUserPass())) {
	            getUserData(user.getUserId(), session);
	        } else {
	            redirects.addFlashAttribute("loginStatus", "Wrong password");
	        }
		}
		else {
			redirects.addFlashAttribute("loginStatus", "User with this login doesnt exist");
		}
		
		try {
	        Authentication authentication = new UsernamePasswordAuthenticationToken(
	                userDetails,
	                userDetails.getPassword(),
	                userDetails.getAuthorities()
	        );
	
	        SecurityContext context = SecurityContextHolder.createEmptyContext();
	        context.setAuthentication(authentication);
	        SecurityContextHolder.setContext(context);
	
	        securityContextRepository.saveContext(context, req, res);
		}catch(AuthenticationException e) {
	        redirects.addFlashAttribute("loginStatus", "Invalid username or password");
	        return "redirect:redirectToAccount";
	    }
		
		return "redirect:redirectToAccount";
	}
	
	@PostMapping("register")
	public String registerUser(@Valid @ModelAttribute("userRegDTO")UserRegDTO userReg,
							BindingResult bindingResult, HttpServletRequest req, Model m,
							RedirectAttributes redirects, HttpServletResponse res) {
		
		if(bindingResult.hasErrors()) {
			redirects.addFlashAttribute("regStatus", "Validation error during registration, check if you filled all fields");
			System.out.println(bindingResult.hasErrors());
			List<FieldError> errors = bindingResult.getFieldErrors();
			
			for(FieldError f : errors) {
				if(f.getField().equals("userName"))
					redirects.addFlashAttribute("regStatus", "Wrong user name format " + f.getDefaultMessage());
				if(f.getField().equals("userPass"))
					redirects.addFlashAttribute("regStatus", "Wrong password format " + f.getDefaultMessage());
			}
			
			return "redirect:redirectToAccount";
		}

		try {
			String rawPassword = userReg.getUserPass();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            userReg.setUserPass(encodedPassword);
			
			userService.saveUser(userReg);
			redirects.addFlashAttribute("regStatus", "New user has been registered, please log in");
			return "redirect:redirectToAccount";
		}catch(DuplicateEntityException e) {
			redirects.addFlashAttribute("regStatus", "Error during registration, try using different username");
			return "redirect:redirectToAccount";
		}
		
		
	}
	
	
	@GetMapping("logout")
	public String userLogout(HttpSession session, HttpServletRequest req) {
		Integer id = (Integer) req.getSession().getAttribute("userId");
		if(id != null) {
			UserDTO u = userService.findById(id);
			if(u != null && u.getType().equalsIgnoreCase("guest")) {
				CartDTO cartDTO = cartService.findCartByUser(id);
				itemService.removeAllCartItems(cartDTO.getCartId());
		    	cartService.removeCart(cartDTO.getCartId());
//		    	userService.removeUser(u.getUserId());
		    }
		}
		
		//???
		session.removeAttribute("userId");
		session.removeAttribute("userName");
		session.removeAttribute("userMail");
//		session.removeAttribute("userPassword");
		session.removeAttribute("userType");
		session.removeAttribute("name");
		session.removeAttribute("surname");
		session.removeAttribute("phoneNumber");
	

		
		return "redirect:redirectToAccount";
	}
	
	@PostMapping("changeUserName")
	public String changeUserName(@RequestParam("userName")String name,
								HttpSession session, HttpServletRequest req,
								RedirectAttributes redirects, HttpServletResponse res) {
		
		Integer userId = (Integer) session.getAttribute("userId");
		String atrName = "updateUserNStatus";
		if(!userService.updateUserName(userId, name)) {

			redirects.addFlashAttribute(atrName, updateMessageErr);
		}
		else {

			redirects.addFlashAttribute(atrName, updateMessageSucc);
		}
		
		getUserData(userId, session);
		
		return "redirect:redirectToAccount";
	}
	
	@ExceptionHandler(UserNameTakenException.class)
    public String handleTypeMismatch(UserNameTakenException ex, RedirectAttributes redirects) {
		
    	redirects.addFlashAttribute("updateUserNStatus", "User name is already taken");
        return "redirect:redirectToAccount";
    }
	
	@PostMapping("changePass")
	public String changePass(@RequestParam("userPass")String pass,
							HttpSession session, HttpServletRequest req,
							RedirectAttributes redirects, HttpServletResponse res) {
		
		Integer userId = (Integer) session.getAttribute("userId");
		String atrName = "updatePassStatus";
		
		String encodedPassword = passwordEncoder.encode(pass);
		
		if(!userService.updatePass(userId, encodedPassword)) {
			redirects.addFlashAttribute(atrName, updateMessageErr);
		}
		else {

			redirects.addFlashAttribute(atrName, updateMessageSucc);
		}
		
		getUserData(userId, session);

		return "redirect:redirectToAccount";
	}
	
	@ExceptionHandler(InvalidPasswordException.class)
    public String handleTypeMismatch(InvalidPasswordException ex, RedirectAttributes redirects) {

    	redirects.addFlashAttribute("updatePassStatus", "Password is too short (min. 6 symbols)");
        return "redirect:redirectToAccount";
    }
	
	@PostMapping("changeMail")
	public String changeMail(@RequestParam @Email String userMail, BindingResult bindingResult,
							HttpSession session, HttpServletRequest req,
							RedirectAttributes redirects, HttpServletResponse res) {
		if(bindingResult.hasErrors())
			redirects.addFlashAttribute("updateMailStatus", "Invalid email format");
		Integer userId = (Integer) session.getAttribute("userId");
		String atrName = "updateMailStatus";
		if(!userService.updateMail(userId, userMail)) {
			redirects.addFlashAttribute(atrName, updateMessageErr);
		}
		else {
			redirects.addFlashAttribute(atrName, updateMessageSucc);
		}
		
		getUserData(userId, session);

		return "redirect:redirectToAccount";
	}
	
//	@ExceptionHandler(ConstraintViolationException.class)
//    public String handleTypeMismatch(ConstraintViolationException ex, RedirectAttributes redirects) {
//
//    	redirects.addFlashAttribute("updateMailStatus", "Invalid email format");
//        return "redirect:redirectToAccount";
//    }
	
	@PostMapping("changeName")
	public String changeName(@RequestParam("name")String name,
							HttpSession session, HttpServletRequest req,
							RedirectAttributes redirects, HttpServletResponse res) {
	
		Integer userId = (Integer) session.getAttribute("userId");
		String atrName = "updateNameStatus";
		if(!userService.updateName(userId, name)) {
			redirects.addFlashAttribute(atrName, updateMessageErr);
		}
		else {
			redirects.addFlashAttribute(atrName, updateMessageSucc);
		}
		
		getUserData(userId, session);

		return "redirect:redirectToAccount";
	}
	
	@PostMapping("changeSurname")
	public String changeSurname(@RequestParam("surname")String surname,
								HttpSession session, HttpServletRequest req,
								RedirectAttributes redirects, HttpServletResponse res) {
		
		Integer userId = (Integer) session.getAttribute("userId");
		String atrName = "updateSurnameStatus";
		if(!userService.updateSurname(userId, surname)) {
			redirects.addFlashAttribute(atrName, updateMessageErr);
		}
		else {
			redirects.addFlashAttribute(atrName, updateMessageSucc);
		}
		
		getUserData(userId, session);

		return "redirect:redirectToAccount";
	}
	
	@PostMapping("changePhoneNumber")
	public String changePhoneNumber(@RequestParam @NotNull Integer phoneNumber,
									HttpSession session, HttpServletRequest req,
									RedirectAttributes redirects, HttpServletResponse res) {
		
		Integer userId = (Integer) session.getAttribute("userId");
		String atrName = "updatePhoneNumberStatus";
		if(!userService.updatePhoneNumber(userId, phoneNumber)) {
			redirects.addFlashAttribute(atrName, updateMessageErr);
		}
		else {
			redirects.addFlashAttribute(atrName, updateMessageSucc);
		}
		
		getUserData(userId, session);

		return "redirect:redirectToAccount";
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, RedirectAttributes redirects) {
        if(ex.getName().equalsIgnoreCase("phonenumber"))
        	redirects.addFlashAttribute("updatePhoneNumberStatus", "Number is too long");
        return "redirect:redirectToAccount";
    }
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleTypeMismatch(MissingServletRequestParameterException ex, HttpSession session) {
		Integer userId = (Integer) session.getAttribute("userId");
		userService.updatePhoneNumber(userId, 0);
        return "redirect:redirectToAccount";
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
	
	public void getUserData(Integer userId, HttpSession session) {
		UserDTO user = userService.findById(userId);
		if(user != null) {
			session.setAttribute("userId", user.getUserId());
			session.setAttribute("userName",  user.getUserName());
			session.setAttribute("userMail",  user.getUserMail());
//			session.setAttribute("userPassword",  user.getUserPass());
			session.setAttribute("userType",  user.getType());
			session.setAttribute("name",  user.getName());
			session.setAttribute("surname",  user.getSurname());
			session.setAttribute("phoneNumber",  user.getPhoneNumber());
		}
	}
}
