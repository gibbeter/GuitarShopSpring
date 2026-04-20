package com.example.demo.product;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.cartitem.ItemDTO;
import com.example.demo.cartitem.ItemRepo;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.InvalidBackUpPathException;
import com.example.demo.files.BackUpService;
import com.example.demo.product.ProductDTO;
import com.example.demo.product.ProductRepo;
import com.example.demo.user.UserController;
import com.opencsv.CSVWriter;

import jakarta.validation.ValidationException;
import model.Cartitem;
import model.Product;
import model.Type;

@Service
public class ProductService {
	
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	TypeRepo typeRepo;
	
	@Autowired
	ItemRepo itemRepo;
	
	@Autowired
	OverviewRepo overRepo;
	
	@Autowired
	BackUpService backupService;
	
	private static final Logger log = LoggerFactory.getLogger(ProductService.class);

	public boolean saveProduct(ProductDTO productDTO) {
		try {
			Product newP = new Product();
			newP.setProductName(productDTO.getProductName());
			newP.setProductDesc(productDTO.getProductDesc());
			newP.setTypeBean(typeRepo.findById(productDTO.getProductType()).get());
			newP.setStock(productDTO.getProductStock());
			newP.setPrice(productDTO.getProductPrice());
			productRepo.save(newP);
			
			saveToCSV(productDTO);

			return true;
		}
		catch(ValidationException e) {
		    log.warn("Validation error saving product: {}", e.getMessage());
		    throw new BusinessException("VALIDATION_ERROR", e.getMessage());
		}
		catch(Exception e) {
			log.warn("Error saving product: {}", e.getMessage());
			throw new BusinessException("PRODUCT_SAVE_ERROR", e.getMessage());
		}
	}
	
	private void saveToCSV(ProductDTO productDTO){
        try{
        	List<ProductDTO> productsList = productsToDTO(productRepo.findAll());
    		String prefix = "products";
    		String fileName = "products";
            String[] header = {"prod_id", "stock", "price", "type", "product_name", "product_desc"};
            
            Function<ProductDTO, String[]> productMapper = product -> new String[]{
            	    product.getProdId().toString(),
            	    product.getProductStock().toString(),
            	    product.getProductPrice().toString(),
            	    product.getProductType().toString(),
            	    product.getProductName(),
            	    product.getProductDesc()
            	};
            backupService.saveBackup(productsList, prefix, fileName, header, productMapper);
        } catch(InvalidBackUpPathException e) {
			log.error("Backup error: {}", e.getMessage());
		}
	}
	
	
	private List<ProductDTO> productsToDTO(List<Product> list){
		List<ProductDTO> res = new ArrayList<>();
		if(list != null) {
			for(Product product: list) {
				ProductDTO productDTO = new ProductDTO(product.getProdId(), product.getProductDesc(),
						product.getProductName(), product.getStock(),
						product.getTypeBean().getTypeId(), product.getTypeBean().getName(),
						product.getPrice());
				res.add(productDTO);
			}
		}
		return res;
	}

	@Transactional
	public boolean modifyProduct(ProductDTO productDTO) {
		try {
			Product newP = productRepo.findById(productDTO.getProdId())
    								.orElseThrow(() -> new EntityNotFoundException("Product", productDTO.getProdId()));
			Type type = typeRepo.findById(productDTO.getProductType())
    							.orElseThrow(() -> new EntityNotFoundException("Type", productDTO.getProductType()));
			newP.setProductName(productDTO.getProductName());
			newP.setProductDesc(productDTO.getProductDesc());
			newP.setTypeBean(type);
			newP.setStock(productDTO.getProductStock());
			productRepo.save(newP);
			
			saveToCSV(productDTO);
			return true;
		}
		catch(ValidationException e) {
			e.printStackTrace();
			return false;
		}
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
			Type t = typeRepo.findByName(typeDTO.getTypeName());
			if(t != null) {
				return t;
			}

			Type newType = new Type();
			newType.setName(typeDTO.getTypeName());
			return typeRepo.save(newType);
			
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}

	public List<TypeDTO> findTypes() {
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

	public ProductDTO findProdById(Integer prodId) {
		Product p = productRepo.findById(prodId)
        		.orElseThrow(() -> new EntityNotFoundException("Product", prodId));
		ProductDTO pDTO= new ProductDTO(p.getProdId(), p.getProductDesc(), p.getProductName(), p.getStock(),
									p.getTypeBean().getTypeId(), p.getTypeBean().getName(), p.getPrice());
		return pDTO;
	}
	
	public boolean changeStock(Integer prodId, Integer q) {
		try {
			Product p = productRepo.findById(prodId)
			        				.orElseThrow(() -> new EntityNotFoundException("Product", prodId));
			p.setStock(p.getStock() - q);
			productRepo.save(p);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Transactional
	public boolean deleteProduct(ProductDTO productDTO) {
		try {
			itemRepo.deleteAllByProductId(productDTO.getProdId());
			overRepo.deleteAllByProductId(productDTO.getProdId());
			
			Product p = productRepo.findById(productDTO.getProdId())
			        				.orElseThrow(() -> new EntityNotFoundException("Product", productDTO.getProdId()));
			productRepo.delete(p);
			
			saveToCSV(productDTO);
			return true;

		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}
}
