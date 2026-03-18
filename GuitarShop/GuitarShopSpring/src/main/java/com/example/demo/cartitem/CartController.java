package com.example.demo.cartitem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.product.ProductDTO;
import com.example.demo.product.ProductService;
import com.example.demo.user.UserDTO;
import com.example.demo.user.UserHelper;
import com.example.demo.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import model.Cartitem;
import model.CartitemPK;
import model.Product;
import model.Type;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import java.io.OutputStream;

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
	
	@Autowired
	UserHelper helper;
	
	@GetMapping("redirectToCart")
	public String redirectToStorage(HttpSession session, HttpServletRequest req, HttpServletResponse res, Model m) {
		
		res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    res.setHeader("Pragma", "no-cache");
	    res.setDateHeader("Expires", 0);
		
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null || helper.checkIsUserAlive(userId) == null) {
			session.removeAttribute("userId");
			req.getSession().setAttribute("guestStatus", "You are buying as guest");
			UserDTO guest = createGuest();
			userId = guest.getUserId();
			session.setAttribute("userId", guest.getUserId());
			session.setAttribute("userName", guest.getUserName());

			return "redirect:/cart/redirectToCart";
		}
		
//		UserDTO guest = null;
//		UserDTO user = userService.findById(userId.get());
//		if(!userId.isPresent() || userService.findById(userId.get()) == null){
			
//		}
//		else {
		if(userService.findById(userId).getType().contains("guest"))
			req.getSession().setAttribute("guestStatus", "You are buying as guest");
		else
			req.getSession().removeAttribute("guestStatus");
//		}
		
		cartService.updateSumm(userId);
		CartDTO cartDTO = cartService.findCartByUser(userId);
		if(cartDTO == null) {
			cartDTO = cartService.newCart(userId);
		}
		List<ItemDTO> items = cartDTO.getCartitems();
//			List<ProductDTO> products = new ArrayList<>();
//			for(Cartitem item: items) {
//				products.add(productService.findProdById(item.getId().getProdId()));
//			}
		m.addAttribute("cartDTO", cartDTO);
//		session.setAttribute("cartDTO", cartDTO);
		session.setAttribute("items", items);
		
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
							HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		if(stock > 0) {
			Integer userId = (Integer) req.getSession().getAttribute("userId");
			if(userId == null) {
				UserDTO guest = createGuest();
				userId = guest.getUserId();
				session.setAttribute("userId", guest.getUserId());
				session.setAttribute("userName", guest.getUserName());
			}
			//cart
			CartDTO cartDTO = updateCart(userId);
			
			//item add
			ItemDTO itemDTO = updateCartitem(cartDTO, prodId);
		}
		if(type.isPresent())
			return "redirect:/product/redirectToType?type="+type.get()+"&prodId=" + prodId+"&itemStatus=added";
		return "redirect:/product/redirectToProductPage?itemStatus=added&prodId=" + prodId;
	}
	
	public CartDTO updateCart(Integer userId) {
		CartDTO cartDTO = cartService.findCartByUser(userId);
		if(cartDTO == null) {
			cartDTO = cartService.newCart(userId);
		}
		return cartDTO;
	}
	
	public ItemDTO updateCartitem(CartDTO cartDTO, Integer prodId) {
		ItemDTO itemDTO = cartService.findItemInCart(cartDTO.getCartId(), prodId);
		if(itemDTO == null) {
			itemDTO = itemService.saveItem(cartDTO.getCartId(), prodId);
		}
		return itemDTO;
	}
	
	@ModelAttribute("cartDTO")
	public CartDTO getCartDTO() {
		return new CartDTO();
	}
	//@ModelAttribute("cartDTO")CartDTO cartToBuy,
	@PostMapping("purchaseCart")
	public String purchase(@RequestParam("cartIds") Integer[] cartIds,
						@RequestParam("productIds") Integer[] productIds,
			            @RequestParam("quantities") Integer[] quantities,
//			            @RequestParam("products") ProductDTO[] products,
			            HttpSession session, HttpServletResponse res, Model m) {
//		if(req.getSession().getAttribute("check") != null)
//			req.getSession().setAttribute("check", req.getSession().getAttribute("check"));
		List<ItemDTO> items = new ArrayList<>();
		CartDTO cartDTO = cartService.findCart(cartIds[0]);
		System.out.println(cartDTO);
		//check
	    JasperPrint report = getCheck(cartDTO.getUserId(), res, m);
	    if(report != null) {
	    	System.out.println(session.getAttribute("check"));
	    	session.setAttribute("check", report);
	    }
		
	    
	    for (int i = 0; i < productIds.length; i++) {
	        ItemDTO item = new ItemDTO();
	        item.setId(new CartitemPK(cartIds[i], productIds[i]));
	        item.setQuantity(quantities[i]);
//	        item.setProduct(products[i]);
	        item.setProduct(productService.findProdById(productIds[i]));
	        items.add(item);
	        System.out.println(item);
	        //change stock
	        productService.changeStock(productIds[i], quantities[i]);
	        //remove cartitem
	        itemService.removeItem(item.getId());
	    }
	    
//	    CartDTO cartDTO = cartService.findCart(items.get(0).getId().getCartId());
	    
	    cartService.purchaseCart(cartDTO.getCartId());
	    UserDTO user = userService.findById(cartDTO.getUserId());
	    if(user.getType().equalsIgnoreCase("guest")) {
	    	itemService.removeAllCartItems(cartDTO.getCartId());
	    	cartService.removeCart(cartDTO.getCartId());
	    	userService.removeUser(cartDTO.getUserId());
	    }
	    
//	    if(cartDTO != null)
    	return "redirect:redirectToCheck";
//	    else
//	    	return "redirect:redirectToCart";
//	    return "redirect:/cart/redirectToCheck?userId=" + cartDTO.getUserId();
//		try {
//			res.sendRedirect("/GuitarShop/cart/redirectToCart?userId=" + cartDTO.getUserId());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	@GetMapping("redirectToCheck")
	public String redirectToCheckPage(@RequestParam("checkStatus")Optional<String> checkStatus, HttpServletRequest req, HttpServletResponse res) {
		
		if(checkStatus.isPresent())
			req.setAttribute("checkStatus", checkStatus.get());
		return "pages/check";
	}
	
	
	
//	if(req.getSession().getAttribute("check") != null)
//		postCheck((JasperPrint)req.getSession().getAttribute("check"), res);
	
	@GetMapping("check")
	public JasperPrint getCheck(Integer userId, HttpServletResponse response, Model m) {
		try {
			JasperPrint jasperReport = cartService.createCheck(userId);
			return jasperReport;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("downloadCheck")
	public String downloadCheck(HttpSession session, HttpServletResponse res) {
		JasperPrint check = (JasperPrint)session.getAttribute("check");
		if(check != null) {
			postCheck(check, res);
			return "index";
		}
		else {
			return "redirect:redirectToCheck?checkStatus=\"Check was deleted from session\"";
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
			e.printStackTrace();
		}
		
	}
	
	@PostMapping("changeQuantity")
	public String changeQuantity(@RequestParam("cartId") Integer cartId,
							@RequestParam("productId") Integer productId,
				            @RequestParam("quantity") Integer quantity,
				            HttpServletResponse res,
				            HttpServletRequest req) {


		itemService.saveItemQuantity(cartId, productId, quantity);

		return "redirect:/cart/redirectToCart";
	}
	
	@PostMapping("deleteItem")
	public String deleteItem(@RequestParam("cartId") Integer cartId,
							@RequestParam("productId") Integer productId,
				            HttpServletResponse res,
				            HttpServletRequest req) {

		itemService.deleteItemFromCart(cartId, productId);

		return "redirect:/cart/redirectToCart";
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
