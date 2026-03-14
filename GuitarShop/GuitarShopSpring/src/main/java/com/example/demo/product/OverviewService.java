package com.example.demo.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.product.ProductDTO;
import com.example.demo.product.ProductRepo;

import jakarta.validation.ValidationException;
import model.Overview;
import model.OverviewPK;
import model.Product;
import model.Type;

@Service
public class OverviewService {
	
	@Autowired
	OverviewRepo overviewRepo;

	public OverviewDTO findOverview(Integer userId, Integer prodId) {
		try {
			Overview o = overviewRepo.findById(new OverviewPK(userId, prodId)).get();
			OverviewDTO newO = new OverviewDTO(o.getId(), o.getRating(), o.getText());
			return newO;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	
	public List<OverviewDTO> findOverviews(Integer prodId) {
		try {
			List<Overview> list = overviewRepo.findByProduct(prodId);
			List<OverviewDTO> res = new ArrayList<>();
			
			if(list == null) {
				return res;
			}
			
			for(Overview o: list) {
				res.add(new OverviewDTO(o.getId(), o.getRating(), o.getText()));
			}
			return res;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}


	public boolean newOverview(OverviewDTO overDTO) {
		try {
			Overview o = new Overview();
			if(overDTO != null) {
				o.setId(overDTO.getId());
				o.setRating(overDTO.getRating());
				o.setText(overDTO.getText());
				return overviewRepo.save(o) != null;
			}
			else {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
