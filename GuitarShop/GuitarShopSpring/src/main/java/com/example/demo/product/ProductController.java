package com.example.demo.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.example.demo.user.UserHelper;
import com.example.demo.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
	public String redirectToStorage(@ModelAttribute(value="delStatus")Optional<String> delStatus, Model m, HttpServletResponse res) {
		
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
	public String redirectToModifyProductPage(@RequestParam("prodId") Optional<Integer> prodId, @ModelAttribute(value="modStatus")Optional<String> modStatus, Model m) {
		if(prodId.isPresent()) {
			m.addAttribute("product", productService.findProdById(prodId.get()));
		}
		else {
		    return "redirect:redirectToStorage";
		}
		
		if(modStatus.isPresent())
			m.addAttribute("modStatus", modStatus.get());
		return "pages/modify_product";
	}
	
	@PostMapping("modifyProductData")
	public String modifyProduct(@Valid @ModelAttribute("productDTO")ProductDTO prodDTO, RedirectAttributes redirects, Model m) {
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
//		return "redirect:redirectToModifyProductPage?prodId="+prodDTO.getProdId() +"&modStatus=" + modStatus;
//		redirects.addFlashAttribute("prodId", prodDTO.getProdId());
		redirects.addFlashAttribute("modStatus", modStatus);
		return "redirect:redirectToModifyProductPage?prodId=" + prodDTO.getProdId();
	}
	
	@PostMapping("deleteProductData")
	public String changeproduct(@Valid @ModelAttribute("productDTO")ProductDTO prodDTO, RedirectAttributes redirects, Model m) {
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
//		return "redirect:redirectToStorage?delStatus=" + delStatus;
		redirects.addFlashAttribute("delStatus", delStatus);
		return "redirect:redirectToStorage";
	}
	
	@ModelAttribute("productDTO")
	public ProductDTO getProductDTO() {
		return new ProductDTO();
	}
	
	@ModelAttribute("createProductDTO")
	public CreateProductDTO getCreateProductDTO() {
		return new CreateProductDTO();
	}
	
	@ModelAttribute("typeDTO")
	public TypeDTO getTypeDTO() {
		return new TypeDTO();
	}
	
	@PostMapping("addProduct")
	public String addProduct(@Valid @ModelAttribute("createProductDTO")CreateProductDTO createProdDTO,
							@Valid @ModelAttribute("typeDTO")TypeDTO typeDTO,
							HttpServletRequest req, RedirectAttributes redirects, HttpServletResponse res) {
//		System.out.println(prodDTO);
//		System.out.println(typeDTO);
		Type type = productService.saveType(typeDTO);
//		System.out.println(createProdDTO.getProductTypeName());
		ProductDTO prodDTO = new ProductDTO(
				createProdDTO.getProductName(),
				createProdDTO.getProductDesc(),
				type.getTypeId(),
				type.getName(),
				createProdDTO.getProductStock(),
				createProdDTO.getProductPrice()
				);
//		prodDTO.setProductType(type.getTypeId());
		try{
			productService.saveProduct(prodDTO);
			redirects.addFlashAttribute("delStatus", "Product added successfully");
		}catch(BusinessException e) {
		    redirects.addFlashAttribute("delStatus", e.getMessage());
		}
		
		return "redirect:redirectToStorage";
	}
	
//	@GetMapping("redirectToTypeCaller")
//	public String redirectToTypeCaller(@RequestParam(value = "type", required = false) Optional<String> type,
//								RedirectAttributes redirects) {
//		if(type.isPresent())
//			redirects.addFlashAttribute("type", type.get());
//		return "redirect:redirectToType";
//	}
	
	@GetMapping("redirectToType")
	public String redirectToType(@RequestParam(value = "type") Optional<String> type,
								HttpServletRequest req, Model m) {
		String itemStatus = (String) m.getAttribute("itemsStatus");
	    Integer prodId = (Integer) m.getAttribute("addedProdId");

		if(type.isPresent()) {
			String formattedType = formatedTypes.get(type.get());
			m.addAttribute("filter",type.get());
			m.addAttribute("type", formattedType);
			m.addAttribute("products", productService.findProdByType(type.get()));
		}
		else {
			m.addAttribute("products", productService.findProducts());
		}
		if(itemStatus != null) {
			System.out.println(itemStatus);
			m.addAttribute("itemsStatus", "Item was added to your cart!");
		}
		if(prodId != null) {
			m.addAttribute("addedProdId", prodId);
		}
		return "pages/products";
	}
	
	
	@GetMapping("redirectToProductPage")
	public String redirectToProduct(@RequestParam(value = "prodId") Optional<Integer> prodId,
									@ModelAttribute(value = "overStatus") Optional<String> overStatus,
									@ModelAttribute(value = "chatStatus") Optional<String> chatStatus,
									@ModelAttribute(value = "itemStatus") Optional<String> itemStatus,
									RedirectAttributes redirects, Model m) {
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
	
//	@ModelAttribute("overviewDTO")
//	public OverviewDTO getOverviewDTO() {
//		return new OverviewDTO();
//	}
	
	@ModelAttribute("createOverviewDTO")
	public CreateOverviewDTO getCreateOverviewDTO() {
		return new CreateOverviewDTO();
	}
	
	
	@PostMapping("postOverview")
	public String postOverview(@Valid @ModelAttribute("createOverviewDTO")CreateOverviewDTO createOverDTO,
							@RequestParam("prodId")Integer prodId,
							HttpSession session, Model m,
							RedirectAttributes redirects, HttpServletResponse res) {
		String overStatus = null;
		Integer userId = (Integer) session.getAttribute("userId");
		try {
			overviewService.postOverview(userId, prodId, createOverDTO);
			overStatus = "You overview was posted";
//			redirects.addFlashAttribute("prodId", prodId);
			redirects.addFlashAttribute("overStatus", overStatus);
			return "redirect:redirectToProductPage?prodId=" + prodId;
		}catch(AccessDeniedException | DuplicateEntityException e) {
			overStatus = e.getMessage();
		}
		catch(NullPointerException e) {
			overStatus = "Internal error";
		}
//		redirects.addFlashAttribute("prodId", prodId);
		redirects.addFlashAttribute("overStatus", overStatus);
		return "redirect:redirectToProductPage?prodId=" + prodId;
	}
	
}
