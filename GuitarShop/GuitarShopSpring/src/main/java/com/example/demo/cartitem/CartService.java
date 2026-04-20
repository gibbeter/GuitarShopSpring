package com.example.demo.cartitem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.GuitarShopSpringApplication;
import com.example.demo.adress.StoreAdressService;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.DuplicateEntityException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.order.OrderDTO;
import com.example.demo.order.OrderItemDTO;
import com.example.demo.order.OrderItemService;
import com.example.demo.order.OrderService;
import com.example.demo.product.ProductDTO;
import com.example.demo.product.ProductRepo;
import com.example.demo.product.ProductService;
import com.example.demo.user.UserService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import model.Cart;
import model.Cartitem;
import model.CartitemPK;
import model.OrderitemPK;
import model.Product;
import model.Type;
import model.User;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class CartService {

//    private final GuitarShopSpringApplication guitarShopSpringApplication;
	
	@Autowired
	CartRepo cartRepo;
	
	@Autowired
	ItemRepo itemRepo;
	
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	ProductService productService;
	
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderItemService orderItemService;
	
	
	private static final Logger log = LoggerFactory.getLogger(CartService.class);


	public CartDTO findCartByUser(Integer userId) {
		try {
			Optional<Cart> existing = cartRepo.findByUser(userId);
			
			if (existing.isEmpty()) {
				log.warn("Cart not found with userId: {}", userId);
			    throw new EntityNotFoundException("CartItem", userId);
			}
			
			Cart c = existing.get();
			CartDTO newC = new CartDTO(c.getCartId() ,c.getSumm(), c.getUserId(), cartItemsToDTO(c.getCartitems()));
			return newC;
			
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	public CartDTO newCart(Integer userId) {
		Cart c = new Cart();
		c.setUserId(userId);
		c = cartRepo.saveAndFlush(c);
		CartDTO newC = new CartDTO(c.getCartId(), c.getSumm(), c.getUserId(), cartItemsToDTO(c.getCartitems()));
		return newC;
	}

	public ItemDTO findItemInCart(Integer cartId, Integer prodId) {
		try {
			CartitemPK pk = new CartitemPK(cartId, prodId);
			Optional<Cartitem> existing = itemRepo.findItemInCart(pk);
			
			if (existing.isEmpty()) {
				log.warn("CartItem not found with id: {}", pk);
				return null;
			}
			
			Cartitem item = existing.get(); 
			Product product = item.getProduct();
			ProductDTO productDTO = new ProductDTO(product.getProdId(), product.getProductDesc(),
												product.getProductName(), product.getStock(),
												product.getTypeBean().getTypeId(), product.getTypeBean().getName(),
												product.getPrice());

			ItemDTO newI = new ItemDTO(item.getId(), item.getQuantity(), productDTO);
			return newI;
			
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
		
	}
	
	public List<ItemDTO> cartItemsToDTO(List<Cartitem> list){
		List<ItemDTO> res = new ArrayList<>();
		if(list != null) {
			for(Cartitem item: list) {
				Product product = item.getProduct();
				ProductDTO productDTO = new ProductDTO(product.getProdId(), product.getProductDesc(),
						product.getProductName(), product.getStock(),
						product.getTypeBean().getTypeId(), product.getTypeBean().getName(),
						product.getPrice());
				res.add(new ItemDTO(item.getId(), item.getQuantity(), productDTO));
			}
		}
		
		return res;
	}

	public CartDTO findCart(int cartId) {
		try {
			Cart c = findCartById(cartId);
			CartDTO cartDTO = new CartDTO(c.getCartId(), c.getSumm(), c.getUserId(), cartItemsToDTO(c.getCartitems()));
			return cartDTO;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	public boolean purchaseCart(int cartId) {
		try {
			Cart c = findCartById(cartId);
			c.setCartitems(new ArrayList<Cartitem>());
			cartRepo.save(c);
			return true;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	public void removeCart(int cartId) {
		try {
			Cart c = findCartById(cartId);
			cartRepo.delete(c);
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}
	
	public boolean updateSumm(Integer userId) {
		try {
			Optional<Cart> existing = cartRepo.findByUser(userId);
			
			if (existing.isEmpty()) {
				log.warn("Cart not found with userId: {}", userId);
			    throw new EntityNotFoundException("CartItem", userId);
			}
			
			Cart c = existing.get();
			List<Cartitem> list = c.getCartitems();
			int summ = 0;
			for(Cartitem i: list) {
				summ += i.getQuantity() * i.getProduct().getPrice();
			}
			c.setSumm(summ);
			return cartRepo.save(c) != null;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	public JasperPrint createCheck(Integer userId, PurchaseFormDTO pformDTO) throws JRException, IOException {

		List<Cartitem> list = itemRepo.findItemsByUser(userId);
		List<CheckDTO> checkList = new ArrayList<>();
		
		Optional<Cart> existing = cartRepo.findByUser(userId);
		
		if (existing.isEmpty()) {
			log.warn("Cart not found with userId: {}", userId);
		    throw new EntityNotFoundException("CartItem", userId);
		}
		
		Cart c = existing.get();

		for(Cartitem i: list) {
			checkList.add(new CheckDTO(i, userId));
			System.out.println(checkList.getLast());
		}
		
		
		
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(checkList);
		InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/checkForm.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cartSumm", c.getSumm());
		params.put("currentDate", new Date());
		params.put("name", pformDTO.getUserName());
		params.put("surname", pformDTO.getUserSurname());
		params.put("phone", pformDTO.getUserPhone());
		params.put("shippingType", pformDTO.getShippingType());
		params.put("shippingAdress", pformDTO.getSPAdress());
		params.put("pickupAdress", pformDTO.getPUAdress());
//		params.put("brojIzvodjenja", ir.brojIzvodjenja(idPredstava));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
		inputStream.close();
		return jasperPrint;
	}

	public OrderDTO createPurchase(Integer userId, Integer[] cartIds, Integer[] productIds, Integer[] quantities,
			@Valid PurchaseFormDTO pformDTO) {
		
		try {
			CartDTO cartDTO = findCart(cartIds[0]);
	//		System.out.println(cartDTO);
				    
		    //order
		    OrderDTO order = new OrderDTO();
		    order.setUserId(userId);
		    order.setOrderDate(new Date());
		    order.setOrderStatus("NEW");
		    
		    order = orderService.createOrder(order);
		    List<OrderItemDTO> orderItems = new ArrayList<>();
		    
		    for (int i = 0; i < productIds.length; i++) {
		    	//order
		    	ProductDTO product = productService.findProdById(productIds[i]);
		    	OrderItemDTO orderItem = new OrderItemDTO(new OrderitemPK(order.getOrderId(), productIds[i]), product.getProductPrice(), quantities[i]);
		    	orderItemService.saveItem(orderItem);
		    	orderItems.add(orderItem);
		    	
		    	
		        //change stock
		        productService.changeStock(productIds[i], quantities[i]);
		        //remove cartitem
		        itemService.removeItem(new CartitemPK(cartIds[i], productIds[i]));
		    }
		   
		    
		    
		    
		    order.setOrderitems(orderItems);
		    order.setSumm(cartDTO.getSumm());
		    if(pformDTO.getShippingType().contains("Pick")) {
		    	order.setOrderType("PU");
		    	order.setPickupAdress(pformDTO.getPUAdress());
		    }
		    else {
		    	order.setOrderType("SP");
		    	order.setShippingAdress(pformDTO.getSPAdress());
		    }
		    
		    order.setName(pformDTO.getUserName());
		    order.setSurname(pformDTO.getUserSurname());
		    order.setPhoneNumber(pformDTO.getUserPhone());
		    
		    order = orderService.saveOrder(order);
		    purchaseCart(cartDTO.getCartId());

		    
		    return order;
		}catch(EntityNotFoundException e) {
	        log.warn("Entity not found during purchase: {}", e.getMessage());
	        throw e;
	    } catch(DataAccessException e) {
	        log.error("Database error during purchase: {}", e.getMessage());
	        throw new BusinessException("DB_ERROR", "Database error during purchase");
	    }
		
	}
	
	public CartDTO updateCart(Integer userId) {
		CartDTO cartDTO = new CartDTO();
		try {
			cartDTO = findCartByUser(userId);
		}catch(EntityNotFoundException e) {
			cartDTO = newCart(userId);
		}
		return cartDTO;
	}
	
	public ItemDTO updateCartitem(CartDTO cartDTO, Integer prodId) {
		ItemDTO itemDTO = findItemInCart(cartDTO.getCartId(), prodId);
		if(itemDTO == null) {
			itemDTO = itemService.saveItem(cartDTO.getCartId(), prodId);
		}
		return itemDTO;
	}
	
	private Cart findCartById(Integer cartId) {
		try {
			Optional<Cart> existing = cartRepo.findById(cartId);
			
			if (existing.isEmpty()) {
				log.warn("Cart not found with id: {}", cartId);
			    throw new EntityNotFoundException("CartItem", cartId);
			}
			
			return existing.get();
		}catch(DataAccessException e) {
	        log.error("Database error during purchase: {}", e.getMessage());
	        throw new BusinessException("DB_ERROR", "Database error during purchase");
	    }
	}
}
