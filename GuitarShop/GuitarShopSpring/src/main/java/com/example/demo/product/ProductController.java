package com.example.demo.product;

import java.io.IOException;
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
import com.example.demo.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
	
	@GetMapping("redirectToStorage")
	public String redirectToStorage(Model m) {
		m.addAttribute("products", productService.findProducts());
		m.addAttribute("types", productService.findTypes());
		return "pages/storage";
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
	public void addProduct(@ModelAttribute("productDTO")ProductDTO prodDTO, @ModelAttribute("typeDTO")TypeDTO typeDTO, HttpServletRequest req, HttpServletResponse res) {
//		System.out.println(prodDTO);
//		System.out.println(typeDTO);
		Type type = productService.saveType(typeDTO);
		prodDTO.setProductType(type.getTypeId());
		productService.saveProduct(prodDTO);
		
		try {
			res.sendRedirect("redirectToStorage");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@GetMapping("redirectToType")
	public String redirectToType(@RequestParam(value = "type", required = false) Optional<String> type, HttpServletRequest req, Model m) {
		try {
			if(type.isPresent()) {
				m.addAttribute("type", type.get());
				m.addAttribute("products", productService.findProdByType(type.get()));
			}
			else {
				m.addAttribute("products", productService.findProducts());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "pages/products";
	}
	
	
	@GetMapping("redirectToProductPage")
	public String redirectToProduct(@RequestParam(value = "prodId", required = false) Optional<Integer> prodId, @RequestParam(value = "overStatus", required = false) Optional<String> overStatus, HttpServletRequest req, Model m) {
		try {
			if(prodId.isPresent()) {
				m.addAttribute("prodId", prodId.get());
				m.addAttribute("product", productService.findProdById(prodId.get()));
				m.addAttribute("overviews", overviewService.findOverviews(prodId.get()));
			}
//			else {
//				m.addAttribute("product", productService.findProducts());
//			}
			if(overStatus.isPresent()) {
				m.addAttribute("overStatus", overStatus.get());
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
	public void postOverview(@ModelAttribute("overviewDTO")OverviewDTO overDTO, @RequestParam("userId")Integer userId, @RequestParam("prodId")Integer prodId,
							Model m, HttpServletResponse res) {
		String overStatus;
		if(overviewService.findOverview(userId, prodId) != null) {
			m.addAttribute("overStatus", "You already left your overview for this product");
			overStatus = "You already left your overview for this product";
		}
		else {
			overDTO.setId(new OverviewPK(userId, prodId));
			if(overviewService.newOverview(overDTO)) {
				m.addAttribute("overStatus", "You overview was posted");
				overStatus = "You overview was posted";
			}
			else {
				m.addAttribute("overStatus", "Error occured during posting of your overview");
				overStatus = "Error occured during posting of your overview";
			}
		}
		try {
			res.sendRedirect("redirectToProductPage?prodId="+prodId+"&overStatus="+overStatus);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
