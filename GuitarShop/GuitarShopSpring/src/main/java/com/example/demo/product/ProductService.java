package com.example.demo.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.product.ProductDTO;
import com.example.demo.product.ProductRepo;

import jakarta.validation.ValidationException;
import model.Product;
import model.Type;

@Service
public class ProductService {
	
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	TypeRepo typeRepo;

	public boolean saveProduct(ProductDTO productDTO) {
		try {
			Product newP = new Product();
			newP.setProductName(productDTO.getProductName());
			newP.setProductDesc(productDTO.getProductDesc());
			newP.setTypeBean(typeRepo.findById(productDTO.getProductType()).get());
			newP.setStock(productDTO.getProductStock());
			productRepo.save(newP);
		}
		catch(ValidationException e) {
			return false;
		}
		return true;
	}
	
	public List<ProductDTO> findProducts(){
		List<ProductDTO> res = new ArrayList<ProductDTO>();
		List<Product> list = productRepo.findAll();
		for(Product p : list) {
			res.add(new ProductDTO(p.getProdId(), p.getProductDesc(), p.getProductName(), p.getStock(),
								p.getTypeBean().getTypeId(), p.getTypeBean().getName(), p.getPrice()));
		}
		return res;
	}

	public Type saveType(TypeDTO typeDTO) { // save new one or return existing type
		try {
			try {
				if(typeRepo.findByName(typeDTO.getTypeName()) != null) {
					return typeRepo.findByName(typeDTO.getTypeName());
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			Type newType = new Type();
			newType.setName(typeDTO.getTypeName());
			return typeRepo.save(newType);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Object findTypes() {
		List<TypeDTO> res = new ArrayList<TypeDTO>();
		List<Type> list = typeRepo.findAll();
		for(Type t : list) {
			res.add(new TypeDTO(t.getTypeId(), t.getName()));
		}
		return res;
	}

	public List<ProductDTO> findProdByType(String type) {
		List<ProductDTO> res = new ArrayList<ProductDTO>();
		List<Product> list = productRepo.findByType(type);
		for(Product p : list) {
			res.add(new ProductDTO(p.getProdId(), p.getProductDesc(), p.getProductName(), p.getStock(),
								p.getTypeBean().getTypeId(), p.getTypeBean().getName(), p.getPrice()));
		}
		return res;
	}

	public ProductDTO findProdById(Integer id) {
		Product p = productRepo.findById(id).get();
		ProductDTO pDTO= new ProductDTO(p.getProdId(), p.getProductDesc(), p.getProductName(), p.getStock(),
									p.getTypeBean().getTypeId(), p.getTypeBean().getName(), p.getPrice());
		return pDTO;
	}

	public boolean changeStock(Integer prodId, Integer q) {
		try {
			Product prod = productRepo.findById(prodId).get();
			prod.setStock(prod.getStock() - q);
			return productRepo.save(prod) != null;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
