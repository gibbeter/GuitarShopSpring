package com.example.demo.product;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.cartitem.ItemDTO;
import com.example.demo.cartitem.ItemRepo;
import com.example.demo.files.BackUpService;
import com.example.demo.product.ProductDTO;
import com.example.demo.product.ProductRepo;
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

	public boolean saveProduct(ProductDTO productDTO) {
		try {
			Product newP = new Product();
			newP.setProductName(productDTO.getProductName());
			newP.setProductDesc(productDTO.getProductDesc());
			newP.setTypeBean(typeRepo.findById(productDTO.getProductType()).get());
			newP.setStock(productDTO.getProductStock());
			newP.setPrice(productDTO.getProductPrice());
			productRepo.save(newP);
			
			try {
				saveToCSV(productDTO);
				return true;
			}catch(Exception ex) {
				return false;
			}
			
		}
		catch(ValidationException e) {
			return false;
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
        } catch (Exception e) {
            // Handle exception appropriately
            e.printStackTrace();
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
			Product newP = productRepo.findById(productDTO.getProdId()).get();
			newP.setProductName(productDTO.getProductName());
			newP.setProductDesc(productDTO.getProductDesc());
			newP.setTypeBean(typeRepo.findById(productDTO.getProductType()).get());
			newP.setStock(productDTO.getProductStock());
			productRepo.save(newP);
			
			try {
				saveToCSV(productDTO);
				return true;
			}catch(Exception ex) {
				return false;
			}
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

	@Transactional
	public boolean deleteProduct(ProductDTO productDTO) {
		try {
			itemRepo.deleteAllByProductId(productDTO.getProdId());
			overRepo.deleteAllByProductId(productDTO.getProdId());
			
			Product p = productRepo.findById(productDTO.getProdId()).get();
			productRepo.delete(p);
			
			try {
				saveToCSV(productDTO);
				return true;
			}catch(Exception ex) {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}
}
