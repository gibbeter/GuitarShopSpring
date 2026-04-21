package com.example.demo.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.DuplicateEntityException;
import com.example.demo.user.UserDTO;
import com.example.demo.user.UserService;

import jakarta.validation.Valid;
import model.Overview;
import model.OverviewPK;

@Service
public class OverviewService {
	
	@Autowired
	OverviewRepo overviewRepo;
	
	@Autowired
	UserService userService;
	
	private static final Logger log = LoggerFactory.getLogger(OverviewService.class);

	public Optional<OverviewDTO> findOverview(Integer userId, Integer prodId) {
		try {
			OverviewPK pk = new OverviewPK(userId, prodId);
			Optional<Overview> existing = overviewRepo.findById(pk);
			if(existing.isEmpty()) {
				log.warn("No overview with id: {}", pk);
//				throw new EntityNotFoundException("Overview", pk);
			}
			else {
				Overview o = existing.get();
				Optional<OverviewDTO> newO = Optional.of(new OverviewDTO(o.getId(), o.getRating(), o.getText(), o.getUserName()));
				return newO;
			}
			
			return Optional.empty();
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
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
				res.add(new OverviewDTO(o.getId(), o.getRating(), o.getText(), o.getUserName()));
			}
			
			return res;
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}


	public OverviewDTO newOverview(OverviewDTO overDTO) {
		try {
			Overview o = new Overview();
			
			o.setId(overDTO.getId());
			o.setRating(overDTO.getRating());
			o.setText(overDTO.getText());
			o.setUserName(overDTO.getUserName());
			o = overviewRepo.save(o);
			return new OverviewDTO(o.getId(), o.getRating(), o.getText(), o.getUserName());
			
			
//				log.error("Null Ponter: {}", "overDTO is null on newOverview()");
//				throw new NullPointerException();
		}catch(DataAccessException e) {
			log.error("Database error: {}", e.getMessage());
			throw new BusinessException("DB_ERROR", "Database error: " + e.getMessage());
		}
	}


	public void postOverview(Integer userId, Integer prodId, @Valid CreateOverviewDTO createOverDTO) {
		String overStatus = null;
		
//		try {
			if(userId == null || userService.findById(userId).getType().contains("guest")) {
				overStatus = "Guests cant write overviews";
				throw new AccessDeniedException(overStatus);
			}
			
			if(findOverview(userId, prodId).isPresent()) {
				overStatus = "You already left your overview for this product";
				throw new DuplicateEntityException(overStatus);
			}
//		}catch(EntityNotFoundException e) {
			UserDTO user = userService.findById(userId);
			OverviewDTO overDTO = new OverviewDTO(
					new OverviewPK(userId, prodId),
					createOverDTO.getRating(),
					createOverDTO.getText(),
					user.getUserName()
					);
			newOverview(overDTO);
//			overStatus = "You overview was posted";		
//		}	
		
	}

}
