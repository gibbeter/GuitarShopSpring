package com.example.demo.adress;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Storeadress;

@Service
public class StoreAdressService {
	
	@Autowired
	StoreAdressRepo storeAdressRepo;
	
	public List<StoreAdressDTO> findAllStores(){
		try {
			List<Storeadress> list = storeAdressRepo.findAll();
			List<StoreAdressDTO> res = new ArrayList<>();
			for(Storeadress a : list) {
				res.add(adressToDTO(a));
			}
			return res;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public StoreAdressDTO adressToDTO(Storeadress a) {
		return new StoreAdressDTO(a.getStoreId(), a.getStroeAdress());
	}
}
