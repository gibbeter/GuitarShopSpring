package com.example.demo.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping("product")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	OverviewService overviewService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserHelper helper;
	
	private Map<String, String> formatedTypes = new HashMap<>(Map.of(
		    "guitar", "Guitars", 
		    "drums", "Drums",
		    "keys", "Keys",
		    "microphone", "Microphones",
		    "cable", "Cables",
		    "soft", "Software"
		));
	
	@GetMapping("redirectToStorage")
	public String redirectToStorage(@RequestParam(value="delStatus")Optional<String> delStatus, Model m, HttpServletResponse res) {
		
		res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    res.setHeader("Pragma", "no-cache");
	    res.setDateHeader("Expires", 0);
	    
		m.addAttribute("products", productService.findProducts());
		m.addAttribute("types", productService.findTypes());
		if(delStatus.isPresent())
			m.addAttribute("delStatus", delStatus.get());
		return "pages/storage";
	}
	
	@GetMapping("redirectToModifyProductPage")
	public String redirectToModifyProductPage(@RequestParam("prodId") Integer prodId, @RequestParam(value="modStatus")Optional<String> modStatus, Model m) {
		m.addAttribute("product", productService.findProdById(prodId));
		if(modStatus.isPresent())
			m.addAttribute("modStatus", modStatus.get());
		return "pages/modify_product";
	}
	
	@PostMapping("modifyProductData")
	public String modifyProduct(@ModelAttribute("productDTO")ProductDTO prodDTO, Model m) {
		String modStatus = null;
		try {
			if(!productService.modifyProduct(prodDTO)) {
				modStatus = "Error during modification";
			}
			else{
				modStatus = "Product was successfully modificated";
			}
		}catch(Exception e) {
			e.printStackTrace();
			modStatus = "Error during modification";
		}
		return "redirect:redirectToModifyProductPage?prodId="+prodDTO.getProdId() +"&modStatus=" + modStatus;
	}
	
	@PostMapping("deleteProductData")
	public String changeproduct(@ModelAttribute("productDTO")ProductDTO prodDTO, Model m) {
		String delStatus = null;
		try{
			if(!productService.deleteProduct(prodDTO)) {
				delStatus = "Error during deleting";
			}
			else {
				delStatus = "Product was successfully deleted";
			}
		}catch(Exception e) {
			e.printStackTrace();
			delStatus = "Error during deletion";
		}
		return "redirect:redirectToStorage?delStatus=" + delStatus;
	}
	
	@ModelAttribute("productDTO")
	public ProductDTO getProductDTO() {
		return new ProductDTO();
	}
	
	@ModelAttribute("typeDTO")
	public TypeDTO getTypeDTO() {
		return new TypeDTO();
	}
	
	@PostMapping("addProduct")
	public String addProduct(@ModelAttribute("productDTO")ProductDTO prodDTO, @ModelAttribute("typeDTO")TypeDTO typeDTO, HttpServletRequest req, HttpServletResponse res) {
//		System.out.println(prodDTO);
//		System.out.println(typeDTO);
		Type type = productService.saveType(typeDTO);
		prodDTO.setProductType(type.getTypeId());
		productService.saveProduct(prodDTO);
		
		return "redirect:redirectToStorage";
		
	}
	
	@GetMapping("redirectToType")
	public String redirectToType(@RequestParam(value = "type", required = false) Optional<String> type,
								@RequestParam(value = "prodId", required = false) Optional<Integer> prodId,
								@RequestParam(value = "itemStatus", required = false) Optional<String> itemStatus,
								HttpSession session,HttpServletRequest req, Model m) {
		try {
//			Integer userId = (Integer) session.getAttribute("userId");
			if(type.isPresent()) {
				String formattedType = formatedTypes.get(type.get());
				m.addAttribute("filter",type.get());
				m.addAttribute("type", formattedType);
				m.addAttribute("products", productService.findProdByType(type.get()));
			}
			else {
				m.addAttribute("products", productService.findProducts());
			}
			if(itemStatus.isPresent()) {
				m.addAttribute("itemStatus", "Item was added to your cart!");
			}
			if(prodId.isPresent()) {
				m.addAttribute("addedProdId", prodId.get());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "pages/products";
	}
	
	
	@GetMapping("redirectToProductPage")
	public String redirectToProduct(@RequestParam(value = "prodId", required = false) Optional<Integer> prodId,
									@RequestParam(value = "overStatus", required = false) Optional<String> overStatus,
									@RequestParam(value = "chatStatus", required = false) Optional<String> chatStatus,
									@RequestParam(value = "itemStatus", required = false) Optional<String> itemStatus,
									HttpServletRequest req, Model m) {
		try {
	
			if(prodId.isPresent()) {
				List<String> imgs = new ArrayList<>(Arrays.asList("main.webp", "side_l.webp", "side_r.webp", "back.webp", "additional.webp"));
				m.addAttribute("prodId", prodId.get());
				m.addAttribute("product", productService.findProdById(prodId.get()));
				m.addAttribute("productImages", imgs);
				m.addAttribute("overviews", overviewService.findOverviews(prodId.get()));
			}
//			else {
//				m.addAttribute("product", productService.findProducts());
//			}
			if(overStatus.isPresent()) {
				m.addAttribute("overStatus", overStatus.get());
			}
			if(chatStatus.isPresent()) {
				m.addAttribute("chatStatus", chatStatus.get());
			}
			if(itemStatus.isPresent()) {
				m.addAttribute("itemStatus", "Item was added to your cart!");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "pages/product_page";
	}
	
	@PostMapping("filterStoreByType")
	public String filterStoreByType(@RequestParam("filterType")String type, Model m) {
		if(type.equalsIgnoreCase("all")) {
			m.addAttribute("products", productService.findProducts());
		}
		else {
			m.addAttribute("products", productService.findProdByType(type));
		}
		m.addAttribute("filter", type);
		m.addAttribute("types", productService.findTypes());
		return "pages/storage";
	}
	
	@ModelAttribute("overviewDTO")
	public OverviewDTO getOverviewDTO() {
		return new OverviewDTO();
	}
	
	@PostMapping("postOverview")
	public String postOverview(@ModelAttribute("overviewDTO")OverviewDTO overDTO, @RequestParam("prodId")Integer prodId,
							HttpSession session, Model m, HttpServletResponse res) {
		String overStatus = null;
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null || userService.findById(userId).getType().contains("guest")) {
			overStatus = "Guests cant write overviews";
//			m.addAttribute("overStatus", overStatus);
			return "redirect:redirectToProductPage?prodId="+prodId+"&overStatus="+overStatus;
		}
			
		if(userId != null && overviewService.findOverview(userId, prodId) != null) {
			overStatus = "You already left your overview for this product";
//			m.addAttribute("overStatus", overStatus);
			
		}
		else {
			UserDTO user = userService.findById(userId);
			overDTO.setUserName(user.getUserName());
			overDTO.setId(new OverviewPK(userId, prodId));
			overDTO = overviewService.newOverview(overDTO);
			
			if(overDTO != null) {
				overStatus = "You overview was posted";
//				m.addAttribute("overStatus", overStatus);
				
			}
//			else {
//				overStatus = "Error occured during posting of your overview";
////				m.addAttribute("overStatus", overStatus);
//			}
		}
		return "redirect:redirectToProductPage?prodId="+prodId+"&overStatus="+overStatus;
	}
	
}
