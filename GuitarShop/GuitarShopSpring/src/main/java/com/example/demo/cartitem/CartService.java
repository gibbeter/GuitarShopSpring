package com.example.demo.cartitem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.GuitarShopSpringApplication;
import com.example.demo.product.ProductDTO;
import com.example.demo.product.ProductRepo;

import jakarta.validation.ValidationException;
import model.Cart;
import model.Cartitem;
import model.CartitemPK;
import model.Product;
import model.Type;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class CartService {

    private final GuitarShopSpringApplication guitarShopSpringApplication;
	
	@Autowired
	CartRepo cartRepo;
	
	@Autowired
	ItemRepo itemRepo;

    CartService(GuitarShopSpringApplication guitarShopSpringApplication) {
        this.guitarShopSpringApplication = guitarShopSpringApplication;
    }

	public CartDTO findCartByUser(Integer id) {
		try {
			Cart c = cartRepo.findByUser(id);
			if(c != null) {
				CartDTO newC = new CartDTO(c.getCartId() ,c.getSumm(), c.getUserId(), cartItemsToDTO(c.getCartitems()));
				return newC;
			}
			else return null;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
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
			Cartitem item = itemRepo.findItemInCart(new CartitemPK(cartId, prodId));
			if(item != null) {
				Product product = item.getProduct();
				ProductDTO productDTO = new ProductDTO(product.getProdId(), product.getProductDesc(),
													product.getProductName(), product.getStock(),
													product.getTypeBean().getTypeId(), product.getTypeBean().getName(),
													product.getPrice());
//				CartDTO cartDTO = new CartDTO(item.getCart().getCartId(), item.getCart().getSumm(),
//											item.getCart().getUserId(), item.getCart().getCartitems());
				ItemDTO newI = new ItemDTO(item.getId(), item.getQuantity(), productDTO);
				return newI;
			}
			return null;
		}catch(Exception e) {
			return null;
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
			Cart c = cartRepo.findById(cartId).get();
			CartDTO cartDTO = new CartDTO(c.getCartId(), c.getSumm(), c.getUserId(), cartItemsToDTO(c.getCartitems()));
			return cartDTO;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean purchaseCart(int cartId) {
		try {
			Cart c = cartRepo.findById(cartId).get();
			c.setCartitems(new ArrayList<Cartitem>());
			return cartRepo.save(c) != null;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void removeCart(int cartId) {
		try {
			Cart c = cartRepo.findById(cartId).get();
			cartRepo.delete(c);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	
//	public JasperPrint createCheck(Integer userId) throws JRException, IOException {
////		System.out.println("IdPredstave: "+idPredstava);
////		System.out.println("Broj karata: "+kr.getKarteZaPredstavu(idPredstava).size());
//		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(kr.getKarteZaPredstavu(idPredstava));
//		InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/karteZaPredstavu.jrxml");
//		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//		Map<String, Object> params = new HashMap<String, Object>();
////		params.put("predstava", pr.findById(idPredstava).get().getNaziv());
////		params.put("brojIzvodjenja", ir.brojIzvodjenja(idPredstava));
//		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
//		inputStream.close();
//		return jasperPrint;
//	}
}
