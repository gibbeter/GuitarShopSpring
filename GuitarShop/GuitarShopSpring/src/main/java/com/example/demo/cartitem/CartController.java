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
import com.example.demo.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
	
	@GetMapping("redirectToCart")
	public String redirectToStorage(@RequestParam(value="userId", required=false)Optional<Integer> userId, HttpServletRequest req, HttpServletResponse res, Model m) {

		UserDTO guest = null;
//		UserDTO user = userService.findById(userId.get());
		if(!userId.isPresent() || userService.findById(userId.get()) == null){
			req.getSession().setAttribute("guestStatus", "You are buying as guest");
			guest = createGuest();
			req.getSession().setAttribute("userId", guest.getUserId());
			req.getSession().setAttribute("userName", guest.getUserName());

			return "redirect:/cart/redirectToCart?userId=" + guest.getUserId();
		}
		else {
			if(userService.findById(userId.get()).getType().contains("guest"))
				req.getSession().setAttribute("guestStatus", "You are buying as guest");
			else
				req.getSession().removeAttribute("guestStatus");
		}
		
		cartService.updateSumm(userId.get());
		CartDTO cartDTO = cartService.findCartByUser(userId.get());
		if(cartDTO == null) {
			cartDTO = cartService.newCart(userId.get());
		}
		List<ItemDTO> items = cartDTO.getCartitems();
//			List<ProductDTO> products = new ArrayList<>();
//			for(Cartitem item: items) {
//				products.add(productService.findProdById(item.getId().getProdId()));
//			}
		System.out.println(cartDTO.getSumm());
		m.addAttribute("cartDTO", cartDTO);
		req.getSession().setAttribute("cartDTO", cartDTO);
		req.getSession().setAttribute("items", items);
		req.getSession().setAttribute("userId", userId.get());
		return "pages/cart";

	}
	
	@ModelAttribute("itemDTO")
	public ItemDTO getItemDTO() {
		return new ItemDTO();
	}
	
	@PostMapping("addItem")
	public void addItem(@RequestParam("prodId")Integer prodId, @RequestParam("stock")Integer stock, HttpServletRequest req, HttpServletResponse res) {
		if(stock > 0) {
			Integer userId = (Integer) req.getSession().getAttribute("userId");
			if(userId == null) {
				UserDTO guest = createGuest();
				userId = guest.getUserId();
				req.getSession().setAttribute("userId", guest.getUserId());
				req.getSession().setAttribute("userName", guest.getUserName());
			}
			//cart
			CartDTO cartDTO = updateCart(userId);
			
			//item add
			ItemDTO itemDTO = updateCartitem(cartDTO, prodId);
		}
		try {
			res.sendRedirect("/GuitarShop/product/redirectToProductPage?prodId=" + prodId);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			            HttpServletRequest req, HttpServletResponse res, Model m) {
		List<ItemDTO> items = new ArrayList<>();
		CartDTO cartDTO = cartService.findCart(cartIds[0]);
		
		//check
	    JasperPrint report = getCheck(cartDTO.getUserId(), res, m);
	    if(report != null) {
	    	req.getSession().setAttribute("check", report);
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
	    
	    req.getSession().setAttribute("userId", cartDTO.getUserId());
		return "pages/check";
//	    return "redirect:/cart/redirectToCheck?userId=" + cartDTO.getUserId();
//		try {
//			res.sendRedirect("/GuitarShop/cart/redirectToCart?userId=" + cartDTO.getUserId());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
//	@GetMapping("redirectToCheck")
//	public String redirectToCheckPage(@RequestParam("userId")Integer userId, HttpServletRequest req, HttpServletResponse res) {
////		if(req.getSession().getAttribute("check") != null)
////			req.getSession().setAttribute("check", req.getSession().getAttribute("check"));
//		req.getSession().setAttribute("userId", userId);
//		return "pages/check";
//	}
	
	@GetMapping("downloadCheck")
	public void downloadCheck(HttpServletRequest req, HttpServletResponse res) {
		if(req.getSession().getAttribute("check") != null)
			postCheck((JasperPrint)req.getSession().getAttribute("check"), res);
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
	public void changeQuantity(@RequestParam("cartId") Integer cartId,
							@RequestParam("productId") Integer productId,
				            @RequestParam("quantity") Integer quantity,
				            HttpServletResponse res,
				            HttpServletRequest req) {


		itemService.saveItemQuantity(cartId, productId, quantity);

		try {
			int userId = (int) req.getSession().getAttribute("userId");
			res.sendRedirect("/GuitarShop/cart/redirectToCart?userId=" + userId);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
