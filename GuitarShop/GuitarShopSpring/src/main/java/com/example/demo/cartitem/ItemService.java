package com.example.demo.cartitem;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.product.ProductRepo;

import model.Cart;
import model.Cartitem;
import model.CartitemPK;
import model.Product;

@Service
public class ItemService {
	
	@Autowired
	ItemRepo itemRepo;
	
	@Autowired
	CartRepo cartRepo;
	
	@Autowired
	ProductRepo prodRepo;

	public ItemDTO saveItem(int cartId, Integer prodId) {
		Cartitem i = new Cartitem();
		i.setQuantity(1);
		Cart cart = cartRepo.findById(cartId).get();
		Product prod = prodRepo.findById(prodId).get();
		i.setCart(cart);
		i.setProduct(prod);
		cart.getCartitems().add(i);
		cart.setSumm(cart.getSumm() + prod.getPrice());
		prod.getCartitems().add(i);
		i.setId(new CartitemPK(cart.getCartId(), prod.getProdId()));
		
//		prodRepo.save(prod);
//		cartRepo.save(cart);
		i = itemRepo.save(i);

		ItemDTO newI = new ItemDTO(i.getId(), i.getQuantity(), null);
		return newI;
	}

	public boolean saveItemQuantity(Integer cartId, Integer productId, Integer quantity) {
		try {
			Cartitem i = itemRepo.findById(new CartitemPK(cartId, productId)).get();
			if(i != null) {
				i.setQuantity(quantity);
				Cart c = cartRepo.findById(cartId).get();
				Product p = prodRepo.findById(productId).get();
				c.setSumm(c.getSumm() + p.getPrice() * quantity);
				itemRepo.save(i);
				return true;
			}
			return false;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removeItem(CartitemPK id) {
		try {
			Cartitem i = itemRepo.findById(id).get();
			itemRepo.delete(i);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void removeAllCartItems(int cartId) {
		try {
			List<Cartitem> list = cartRepo.findById(cartId).get().getCartitems();
			if(list != null && list.size() > 0) {
				for(Cartitem i: list) {
					itemRepo.delete(i);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public boolean deleteItemFromCart(Integer cartId, Integer productId) {
		try {
			Cart cart = cartRepo.findById(cartId).get();
			Product product = prodRepo.findById(productId).get();
			List<Cartitem> list = cart.getCartitems();
			for(Cartitem i: list) {
				if(i.getProduct().equals(product)) {
					list.remove(i);
					itemRepo.delete(i);
					break;
				}
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
