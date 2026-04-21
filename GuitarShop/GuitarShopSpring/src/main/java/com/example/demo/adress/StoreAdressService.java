package com.example.demo.adress;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.exception.BusinessException;

import model.Storeadress;

@Service
public class StoreAdressService {
	
	@Autowired
	StoreAdressRepo storeAdressRepo;
	
	private static final Logger log = LoggerFactory.getLogger(StoreAdressService.class);
	
	public List<StoreAdressDTO> findAllStores(){
		try {
			List<Storeadress> list = storeAdressRepo.findAll();
			List<StoreAdressDTO> res = new ArrayList<>();
			for(Storeadress a : list) {
				res.add(adressToDTO(a));
			}
			return res;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}
	
	public StoreAdressDTO adressToDTO(Storeadress a) {
		return new StoreAdressDTO(a.getStoreId(), a.getStroeAdress());
	}
}
