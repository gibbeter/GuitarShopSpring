package com.example.demo.order;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.product.ProductDTO;
import com.example.demo.user.UserDTO;
import com.example.demo.user.UserHelper;
import com.example.demo.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import model.OverviewPK;
import model.Type;

@Controller
@RequestMapping("order")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderItemService orderItemService;
	
	private static final Logger log = LoggerFactory.getLogger(OrderController.class);

	@GetMapping("redirectToOrders")
	public String redirectToOrders(@ModelAttribute(value="delStatus")Optional<String> delStatus, Model m) {
		m.addAttribute("orders", orderService.findAllOrders());
		m.addAttribute("filter", "ALL");
		if(delStatus.isPresent())
			m.addAttribute("delStatus", delStatus.get());
		return "pages/orders";
	}
	
	@GetMapping("redirectToUserOrders")
	public String redirectToUserOrders(HttpSession session, Model m) {
		Integer userId = getAuthUserId(session);
		m.addAttribute("orders", orderService.findAllUserOrders(userId));
		m.addAttribute("filter", "ALL");
		return "pages/orders_user";
	}
	
//	@GetMapping("redirectToModifyCaller")
//	public String redirectToModifyCaller(@RequestParam("orderId")Integer orderId, RedirectAttributes redirects) {
//		redirects.addFlashAttribute("orderId", orderId);
//		return "redirect:redirectToModifyOrderPage";
//	}
	
	@GetMapping("redirectToModifyOrderPage")
	public String redirectToOrders(@RequestParam("orderId")Integer orderId,
									@ModelAttribute(value="modStatus")Optional<String> modStatus,
									RedirectAttributes redirects, Model m) {
	    try {
	        OrderDTO orderDTO = orderService.findOrder(orderId);
	        m.addAttribute("order", orderDTO);
//	        m.addAttribute("orderId", orderId);
	    } catch(EntityNotFoundException e) {
	        log.warn("Order not found: {}", e.getMessage());
	        redirects.addFlashAttribute("modStatus", "Order not found");
	        return "redirect:redirectToOrders";
	    } catch(BusinessException e) {
	        log.error("Error loading order: {}", e.getMessage());
	        redirects.addFlashAttribute("modStatus", "Error loading order");
	        return "redirect:redirectToOrders";
	    }
	    
	    return "pages/modify_order";
	}
	
	@ModelAttribute("orderDTO")
	public OrderDTO getOrderDTO() {
		return new OrderDTO();
	}
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
	
	@PostMapping("modifyOrderData")
	public String modifyOrder(@Valid @ModelAttribute("orderDTO")OrderDTO orderDTO,
								BindingResult bind, RedirectAttributes redirects, Model m) {
		if(bind.hasErrors()) {

			log.warn("Validation error, data out of fromat: ", orderDTO.getPhoneNumber());
			List<FieldError> errors = bind.getFieldErrors();

			for(FieldError f : errors) {
				if(f.getField().equals("name"))
					redirects.addFlashAttribute("validMessage", "Wrong name format " + f.getDefaultMessage());
				if(f.getField().equals("surname"))
					redirects.addFlashAttribute("validMessage", "Wrong surname format " + f.getDefaultMessage());
				if(f.getField().equals("phoneNumber"))
					redirects.addFlashAttribute("validMessage", "Wrong phone format " + f.getDefaultMessage());
				if(f.getField().equals("orderType"))
					redirects.addFlashAttribute("validMessage", "Wrong order type format " + f.getDefaultMessage());
				if(f.getField().equals("pickupAdress"))
					redirects.addFlashAttribute("validMessage", "Wrong pick up adress format " + f.getDefaultMessage());
				if(f.getField().equals("shippingAdress"))
					redirects.addFlashAttribute("validMessage", "Wrong shipping adress format " + f.getDefaultMessage());
			}
			return "redirect:redirectToModifyOrderPage?orderId="+orderDTO.getOrderId();
		}
			
		
		String modStatus = null;
		
		try {
			if(!orderService.modifyOrder(orderDTO)) {
				modStatus = "Error during modification";
			}
			else{
				modStatus = "Order was successfully modificated";
			}
		}catch(EntityNotFoundException e) {
		    log.warn("Order not found: {}", e.getMessage());
		    modStatus = "Order not found";
		} catch(BusinessException e) {
		    log.error("Business error: {}", e.getMessage());
		    modStatus = "Error during modification";
		}
//		return "redirect:redirectToModifyOrderPage?orderId="+orderDTO.getOrderId() +"&modStatus=" + modStatus;
//		redirects.addFlashAttribute("orderId", orderDTO.getOrderId());
		redirects.addFlashAttribute("modStatus", modStatus);
		return "redirect:redirectToModifyOrderPage?orderId="+orderDTO.getOrderId();
	}
	
	@PostMapping("deleteOrderData")
	public String deleteOrder(@Valid @ModelAttribute("orderDTO")OrderDTO orderDTO,
								RedirectAttributes redirects, Model m) {
		String delStatus = null;
		try{
			if(!orderService.deleteOrder(orderDTO)) {
				delStatus = "Error during deletion";
			}
			else {
				delStatus = "Order was successfully deleted";
			}
		}catch(EntityNotFoundException e) {
		    log.warn("Order not found: {}", e.getMessage());
		    delStatus = "Error during deletion";
		} catch(BusinessException e) {
		    log.error("Business error: {}", e.getMessage());
		    delStatus = "Error during deletion";
		}
//		return "redirect:redirectToOrders?delStatus=" + delStatus;
		redirects.addFlashAttribute("delStatus", delStatus);
		return "redirect:redirectToOrders";
	}
	
	@GetMapping("filterOrdersByStatus")
	public String filterOrdersByType(@RequestParam("filterStatus")String status, Model m) {
		if(status.equalsIgnoreCase("all")) {
			m.addAttribute("orders", orderService.findAllOrders());
		}
		else {
			m.addAttribute("orders", orderService.findOrderByStatus(status));
		}
		m.addAttribute("filter", status);
		return "pages/orders";
	}
	
	@GetMapping("filterUserOrdersByStatus")
	public String filterUserOrdersByType(@RequestParam("filterStatus")String status, HttpSession session, Model m) {
		Integer userId = getAuthUserId(session);
		if(status.equalsIgnoreCase("all")) {
			m.addAttribute("orders", orderService.findAllUserOrders(userId));
		}
		else {
			m.addAttribute("orders", orderService.findUserOrderByStatus(userId, status));
		}
		m.addAttribute("filter", status);
		return "pages/orders_user";
	}
	
	private Integer getAuthUserId(HttpSession session) {
	    Integer userId = (Integer) session.getAttribute("userId");
	    if(userId == null) {
	        log.warn("Unauthorized access attempt");
	        throw new AccessDeniedException("You must be logged in to access orders");
	    }
	    return userId;
	}
}
