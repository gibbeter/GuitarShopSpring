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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	@GetMapping("redirectToOrders")
	public String redirectToOrders(@RequestParam(value="delStatus")Optional<String> delStatus, Model m) {
		m.addAttribute("orders", orderService.findAllOrders());
		m.addAttribute("filter", "ALL");
		if(delStatus.isPresent())
			m.addAttribute("delStatus", delStatus.get());
		return "pages/orders";
	}
	
	@GetMapping("redirectToUserOrders")
	public String redirectToUserOrders(HttpSession session, Model m) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId != null)
			m.addAttribute("orders", orderService.findAllUserOrders(userId));
		else
			throw new NullPointerException("userId is null");
		m.addAttribute("filter", "ALL");
		return "pages/orders_user";
	}
	
	@GetMapping("redirectToModifyOrderPage")
	public String redirectToOrders(@RequestParam("orderId")Integer orderId, @RequestParam(value="modStatus")Optional<String> modStatus, Model m) {
		OrderDTO orderDTO = orderService.findOrder(orderId);
		if(orderDTO != null)
			m.addAttribute("order", orderDTO);
		if(modStatus.isPresent())
			m.addAttribute("modStatus", modStatus.get());
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
	public String modifyOrder(@ModelAttribute("orderDTO")OrderDTO orderDTO, Model m) {
		String modStatus = null;
		try {
			if(!orderService.modifyOrder(orderDTO)) {
				modStatus = "Error during modification";
			}
			else{
				modStatus = "Order was successfully modificated";
			}
		}catch(Exception e) {
			e.printStackTrace();
			modStatus = "Error during modification";
		}
		return "redirect:redirectToModifyOrderPage?orderId="+orderDTO.getOrderId() +"&modStatus=" + modStatus;
	}
	
	@PostMapping("deleteOrderData")
	public String deleteOrder(@ModelAttribute("orderDTO")OrderDTO orderDTO, Model m) {
		String delStatus = null;
		try{
			if(!orderService.deleteOrder(orderDTO)) {
				delStatus = "Error during deletion";
			}
			else {
				delStatus = "Order was successfully deleted";
			}
		}catch(Exception e) {
			e.printStackTrace();
			delStatus = "Error during deletion";
		}
		return "redirect:redirectToOrders?delStatus=" + delStatus;
	}
	
	@GetMapping("filterOrdersByStatus")
	public String filterStoreByType(@RequestParam("filterStatus")String status, Model m) {
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
	public String filterStoreByType(@RequestParam("filterStatus")String status, HttpSession session, Model m) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(status.equalsIgnoreCase("all")) {
			if(userId != null)
				m.addAttribute("orders", orderService.findAllUserOrders(userId));
			else
				throw new NullPointerException("userId is null");
		}
		else {
			if(userId != null)
				m.addAttribute("orders", orderService.findUserOrderByStatus(userId, status));
			else
				throw new NullPointerException("userId is null");
		}
		m.addAttribute("filter", status);
		return "pages/orders_user";
	}
}
