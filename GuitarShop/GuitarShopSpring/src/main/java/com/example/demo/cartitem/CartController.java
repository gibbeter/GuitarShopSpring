package com.example.demo.cartitem;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.adress.StoreAdressService;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.product.ProductService;
import com.example.demo.user.UserDTO;
import com.example.demo.user.UserHelper;
import com.example.demo.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
@RequestMapping("cart")
public class CartController {
	
	@Autowired
	CartService cartService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	UserService userService;
	
//	@Autowired
//	OrderService orderService;
	
//	@Autowired
//	OrderItemService orderItemService;
	
	@Autowired
	StoreAdressService adressService;
	
	@Autowired
	UserHelper helper;
	
	private static final Logger log = LoggerFactory.getLogger(CartController.class);
	
	@GetMapping("redirectToCart")
	public String redirectToCart(@RequestParam(value="cartErrorStatus", required=false)Optional<String> cartErrorStatus,HttpSession session, HttpServletRequest req, HttpServletResponse res, Model m) {
		
		res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    res.setHeader("Pragma", "no-cache");
	    res.setDateHeader("Expires", 0);
	    
	    if(cartErrorStatus.isPresent()) {
	    	m.addAttribute("cartErrorStatus", cartErrorStatus.get());
	    }
		
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null || helper.checkIsUserAlive(userId) == null) {
			session.removeAttribute("userId");
			req.getSession().setAttribute("guestStatus", "You are buying as guest");
			UserDTO guest = createGuest();
			userId = guest.getUserId();
			session.setAttribute("userId", guest.getUserId());

			return "redirect:/cart/redirectToCart";
		}
		
		//form fields
		
		UserDTO userDTO = userService.findById(userId);
		if(userDTO.getName() != null)
			session.setAttribute("userNameTemp", userDTO.getName());
		if(userDTO.getSurname() != null)
			session.setAttribute("userSurnameTemp", userDTO.getSurname());
		if(userDTO.getPhoneNumber() != null)
			session.setAttribute("userPhoneTemp", userDTO.getPhoneNumber());
		
		
		if(userDTO.getType().contains("guest"))
			req.getSession().setAttribute("guestStatus", "You are buying as guest");
		else
			req.getSession().removeAttribute("guestStatus");

		CartDTO cartDTO = new CartDTO();
		try {
			cartDTO = cartService.findCartByUser(userId);
		}catch(EntityNotFoundException e) {
			cartDTO = cartService.newCart(userId);
		}finally {
			cartService.updateSumm(userId);
			cartDTO = cartService.findCartByUser(userId);
		}
		

		
		List<ItemDTO> items = cartDTO.getCartitems();

		m.addAttribute("cartDTO", cartDTO);
		session.setAttribute("items", items);
		m.addAttribute("pickAdresses", adressService.findAllStores());
		
		//clear check
		JasperPrint check = (JasperPrint) session.getAttribute("check");
	    if (check != null) {
	        session.removeAttribute("check");
	    }
		
		return "pages/cart";

	}
	
	@ModelAttribute("itemDTO")
	public ItemDTO getItemDTO() {
		return new ItemDTO();
	}
	
	@PostMapping("addItem")
	public String addItem(@RequestParam("prodId")Integer prodId,
							@RequestParam("stock")Integer stock,
							@RequestParam(value="type", required=false)Optional<String> type,
							HttpSession session, HttpServletRequest req,
							RedirectAttributes redirects, HttpServletResponse res) {
		if(stock > 0) {
			Integer userId = (Integer) session.getAttribute("userId");
			if(userId == null) {
				UserDTO guest = createGuest();
				userId = guest.getUserId();
				session.setAttribute("userId", guest.getUserId());
				session.setAttribute("userName", guest.getUserName());
			}
			//cart
			CartDTO cartDTO = cartService.updateCart(userId);
			
			//item add
//			ItemDTO itemDTO = cartService.updateCartitem(cartDTO, prodId);
		}
		if(type.isPresent()) {
			redirects.addFlashAttribute("prodId", prodId);
			redirects.addFlashAttribute("itemsStatus", "added");
			redirects.addFlashAttribute("addedId", prodId);
			if(type.get().equals("")) {
//				return "redirect:/product/redirectToType?prodId=" + prodId+"&itemStatus=added";
				return "redirect:/product/redirectToType";
			}
			else {
//				return "redirect:/product/redirectToType?type="+type.get()+"&prodId=" + prodId+"&itemStatus=added";
//				redirects.addFlashAttribute("type", type.get());
				return "redirect:/product/redirectToType?type=" + type.get();
			}
				
		}
//		return "redirect:/product/redirectToProductPage?itemStatus=added&prodId=" + prodId;
//		redirects.addFlashAttribute("prodId", prodId);
		redirects.addFlashAttribute("itemStatus", "Item was added to your cart!");
		return "redirect:/product/redirectToProductPage?&prodId=" + prodId;
	}
	
	
	@ModelAttribute("cartDTO")
	public CartDTO getCartDTO() {
		return new CartDTO();
	}
	
	@ModelAttribute("pformDTO")
	public PurchaseFormDTO getPFormDTO() {
		return new PurchaseFormDTO();
	}
	
	private void saveFormFields(PurchaseFormDTO pformDTO, HttpSession session) {
		if(pformDTO.getUserName() != null)
			session.setAttribute("userNameTemp", pformDTO.getUserName());
		if(pformDTO.getUserSurname() != null)
			session.setAttribute("userSurnameTemp", pformDTO.getUserSurname());
		if(pformDTO.getUserPhone() != null)
			session.setAttribute("userPhoneTemp", pformDTO.getUserPhone());
		if(pformDTO.getShippingType() != null) {
			session.setAttribute("shippingTypeTemp", pformDTO.getShippingType());
			if(pformDTO.getShippingType().contains("Pick")) {
				if(pformDTO.getPUAdress() != null)
					session.setAttribute("PUAdressTemp", pformDTO.getPUAdress());
				session.removeAttribute("SPAdressTemp");
			}
			else {
				if(pformDTO.getSPAdress() != null)
					session.setAttribute("SPAdressTemp", pformDTO.getSPAdress());
				session.removeAttribute("PUAdressTemp");
			}
		}

	}
	
	@PostMapping("purchaseCart")
	public String purchase(@RequestParam("cartIds") Integer[] cartIds,
						@RequestParam("productIds") Integer[] productIds,
			            @RequestParam("quantities") Integer[] quantities,
			            @Valid @ModelAttribute("pformDTO") PurchaseFormDTO pformDTO,
			            BindingResult bindingResult, HttpSession session,
			            HttpServletResponse res, Model m,
			            RedirectAttributes redirects) {

		
		if(bindingResult.hasErrors()) {
			saveFormFields(pformDTO, session);
			List<FieldError> errors = bindingResult.getFieldErrors();

			for(FieldError f : errors) {
				if(f.getField().equals("userName"))
					redirects.addFlashAttribute("cartErrorStatus", "Wrong name format " + f.getDefaultMessage());
				if(f.getField().equals("userSurname"))
					redirects.addFlashAttribute("cartErrorStatus", "Wrong surname format " + f.getDefaultMessage());
				if(f.getField().equals("userPhone"))
					redirects.addFlashAttribute("cartErrorStatus", "Wrong phone format " + f.getDefaultMessage());
				if(f.getField().equals("shippingType"))
					redirects.addFlashAttribute("cartErrorStatus", "Wrong shipping type format " + f.getDefaultMessage());
				if(f.getField().equals("pickupAdress"))
					redirects.addFlashAttribute("cartErrorStatus", "Wrong pick up adress format " + f.getDefaultMessage());
				if(f.getField().equals("shippingAdress"))
					redirects.addFlashAttribute("cartErrorStatus", "Wrong shipping adress format " + f.getDefaultMessage());
			}
			log.warn("Binding result: {}", bindingResult.getAllErrors());
			return "redirect:redirectToCart";
		}
		
		saveFormFields(pformDTO, session);
		
	    Integer userId = (Integer) session.getAttribute("userId");
	    CartDTO cartDTO = cartService.findCart(cartIds[0]);

		
		try {
			JasperPrint report = getCheck(cartDTO.getUserId(), pformDTO, res, redirects, m);
//			OrderDTO order = cartService.createPurchase(userId, cartIds, productIds, quantities, pformDTO);
	        if(report != null)
	        	session.setAttribute("check", report);
		}catch(BusinessException e) {
	        log.error("Purchase error: {}", e.getMessage());
	        redirects.addFlashAttribute("cartErrorStatus", "Error during purchase");
	        return "redirect:redirectToCart";
	    }

    	return "redirect:redirectToCheck";

	}
	
	@GetMapping("redirectToCheck")
	public String redirectToCheckPage() {
		return "pages/check";
	}
	
	
	
	@GetMapping("check")
	public JasperPrint getCheck(Integer userId, PurchaseFormDTO pformDTO, HttpServletResponse response, RedirectAttributes redirects, Model m) {
		try {
			JasperPrint jasperReport = cartService.createCheck(userId, pformDTO);
			return jasperReport;
		} catch(JRException | IOException e) {
			log.error("Purchase error: {}", e.getMessage());
	        throw new BusinessException("CHECK_REPORT_ERROR", "Jasperreport error");
		}
	}
	
	@GetMapping("downloadCheck")
	public String downloadCheck(HttpSession session, RedirectAttributes redirects, HttpServletResponse res) {
		JasperPrint check = (JasperPrint)session.getAttribute("check");
		if(check != null) {
			postCheck(check, res);
			return "index";
		}
		else {
			redirects.addFlashAttribute("checkStatus", "Check was deleted from session");
			return "redirect:redirectToCheck";
		}
	}
	
	public void postCheck(JasperPrint jasperReport, HttpServletResponse response){
		try {
			response.setContentType("text/html");
			response.setContentType("application/x-download");
			response.addHeader("Content-disposition", "attachment; filename=Check:"+new Date().toString()+".pdf");
			OutputStream out = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperReport, out);
			out.close();
		} catch(Exception e) {
			log.error("Purchase error: {}", e.getMessage());
	        throw new BusinessException("CHECK_REPORT_ERROR", "Jasperreport error");
		}
		
	}
	
	@PostMapping("changeQuantity")
	public String changeQuantity(@RequestParam("cartId") Integer cartId,
							@RequestParam("productId") Integer productId,
				            @RequestParam("quantity") Integer quantity) {

		itemService.saveItemQuantity(cartId, productId, quantity);
		return "redirect:/cart/redirectToCart";
	}
	
	@PostMapping("deleteItem")
	public String deleteItem(@RequestParam("cartId") Integer cartId,
							@RequestParam("productId") Integer productId) {

		itemService.deleteItemFromCart(cartId, productId);
		return "redirect:/cart/redirectToCart";
	}
	
	public UserDTO createGuest() {
		try {
			UserDTO guest = userService.createGuestUser();
			return guest;
		}catch(Exception e) {
	        log.error("Error creating guest user: {}", e.getMessage());
	        throw new BusinessException("GUEST_ERROR", "Could not create guest session");
	    }
	}
}
