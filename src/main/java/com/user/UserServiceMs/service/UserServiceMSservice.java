package com.user.UserServiceMs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.UserServiceMs.dto.BuyerDTO;
import com.user.UserServiceMs.dto.BuyerLoginDTO;
import com.user.UserServiceMs.dto.CartDTO;
import com.user.UserServiceMs.dto.SellerDTO;
import com.user.UserServiceMs.dto.SellerLoginDTO;
import com.user.UserServiceMs.dto.WishlistDTO;
import com.user.UserServiceMs.entity.BuyerEntity;
import com.user.UserServiceMs.entity.CartEntity;
import com.user.UserServiceMs.entity.SellerEntity;
import com.user.UserServiceMs.entity.WishlistEntity;
import com.user.UserServiceMs.repository.BuyerRepository;
import com.user.UserServiceMs.repository.CartRepository;
import com.user.UserServiceMs.repository.SellerRepository;
import com.user.UserServiceMs.repository.WishListRepository;
import com.user.UserServiceMs.validator.BuyerValidator;
import com.user.UserServiceMs.validator.SellerValidator;

@Service
public class UserServiceMSservice {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	BuyerRepository buyerRepo;
	
	@Autowired
	SellerRepository sellerRepo;
	
	@Autowired
	WishListRepository wishRepo;
	
	@Autowired
	CartRepository cartRepo;
	
//	Register Buyer
	public Integer createBuyer(BuyerDTO buyerDTO) throws Exception {
		BuyerValidator.validateBuyer(buyerDTO);
		buyerDTO.setPhoneNo(buyerDTO.getPhoneNo().substring(4));
		logger.info("Creation request for Buyer {}", buyerDTO);
		buyerDTO.setIsActive(1);
		BuyerEntity buyer = buyerDTO.createEntity();
		
		return buyerRepo.save(buyer).getBuyerId();
	}
 

//	login Buyer
	public boolean buyerLogin(BuyerLoginDTO buyerLoginDTO) throws Exception {
		BuyerValidator.validateBuyerLogin(buyerLoginDTO);
		logger.info("Login request for buyer {} with password {}", buyerLoginDTO.getEmail(),buyerLoginDTO.getPassword());
		List<BuyerEntity> buyerList = buyerRepo.getByEmail(buyerLoginDTO.getEmail());
		if (!buyerList.isEmpty()) {
			for(BuyerEntity buyerEntity:buyerList) {
				if(buyerEntity.getIsActive()==0) {
					throw new Exception("Buyer.DEACTIVATED");
				}
				if (buyerEntity.getPassword().equals(buyerLoginDTO.getPassword())) {
					return true;
				}
			}
		}

		return false;
	}
	//find specific phoneNumber for Buyer
	public boolean findPhoneNumber(String phoneNumber) {
		logger.info("checking phone number{}",phoneNumber);
		List<BuyerEntity> buyers=buyerRepo.findAll();
		for(BuyerEntity buyer:buyers) {
			if(buyer.getPhoneNumber().equals(phoneNumber)) {
				return true;
			}
		}
		return false;
	}
	//find specific phoneNumber for Seller
		public boolean findPhoneNumberForSeller(String phoneNumber) {
			logger.info("checking phone number{}",phoneNumber);
			List<SellerEntity> sellers=sellerRepo.findAll();
			for(SellerEntity seller:sellers) {
				if(seller.getsPhoneNumber().equals(phoneNumber)) {
					return true;
				}
			}
			return false;
		}
	//find buyer by id
	public boolean findBuyerId(Integer buyerId)
	{
		logger.info("find request for buyer {} with buyerId{}", buyerId);
		Optional<BuyerEntity> buyerEntity =buyerRepo.findById(buyerId);
		if (buyerEntity.isPresent())
		{
			return true;
		}
		return false;
	}
	
	
//	Register seller
	public Integer createSeller(SellerDTO sellerDTO) throws Exception {
		SellerValidator.validateSeller(sellerDTO);
		sellerDTO.setsPhoneNo(sellerDTO.getsPhoneNo().substring(4));
		logger.info("Creation request for seller {}", sellerDTO);
		sellerDTO.setsIsActive(1);
		SellerEntity seller = sellerDTO.createEntity();
		
		return sellerRepo.save(seller).getSellerId();
	}
	
//	login Seller
	public boolean sellerLogin(SellerLoginDTO sellerLoginDTO) throws Exception {
		SellerValidator.validateSellerLogin(sellerLoginDTO);
		logger.info("Login request for seller {} with password {}", sellerLoginDTO.getsEmail(),sellerLoginDTO.getsPassword());
		List<SellerEntity> sellerList = sellerRepo.getBysEmail(sellerLoginDTO.getsEmail());
		if (!sellerList.isEmpty()) {
			for(SellerEntity sellerEntity:sellerList) {
				if(sellerEntity.getIsActive()==0) {
					throw new Exception("Seller.DEACTIVATED");
				}
				if (sellerEntity.getsPassword().equals(sellerLoginDTO.getsPassword())) {
					return true;
				}
			}
		}

		return false;
	}
	//find seller by id
		public boolean findSellerId(Integer sellerId)
		{
			logger.info("find request for seller {} with sellerId{}", sellerId);
			Optional<SellerEntity> sellerEntity =sellerRepo.findById(sellerId);
			if (sellerEntity.isPresent())
			{
				return true;
			}
			return false;
		}
		
	// deactivate seller
		public boolean deactivateSeller(Integer sellerId) {
			logger.info("deactivate request for seller {} with sellerId{}", sellerId);
			Optional<SellerEntity> sellerEntity =sellerRepo.findById(sellerId);
			if (sellerEntity.isPresent())
			{
				SellerEntity seller=sellerEntity.get();
				seller.setIsActive(0);
				sellerRepo.save(seller);
				return true;
				
			}
			return false;
			
		}
	// activate seller
		public boolean activateSeller(SellerLoginDTO sellerLoginDTO) {
			logger.info("activate request for seller {} with email{}", sellerLoginDTO.getsEmail());
			List<SellerEntity> sellerEntities =sellerRepo.getBysEmail(sellerLoginDTO.getsEmail());
			if(!sellerEntities.isEmpty()) {
				for(SellerEntity sellerEntity:sellerEntities) {
					if (sellerEntity.getsPassword().equals(sellerLoginDTO.getsPassword())) {
						sellerEntity.setIsActive(1);
						sellerRepo.save(sellerEntity);
						
					}
				}
				return true;
			}
			return false;
		}
	//activate buyer
		public boolean activateBuyer(BuyerLoginDTO buyerLoginDTO) {
			logger.info("activate request for buyer {} with email{}",buyerLoginDTO.getEmail());
			List<BuyerEntity> buyerEntities =buyerRepo.getByEmail(buyerLoginDTO.getEmail());
			if(!buyerEntities.isEmpty()) {
				for(BuyerEntity buyerEntity:buyerEntities) {
					if (buyerEntity.getPassword().equals(buyerLoginDTO.getPassword())) {
						buyerEntity.setIsActive(1);
						buyerRepo.save(buyerEntity);
						
					}
				}
				return true;
			}
			return false;
		}
		// deactivate buyer
				public boolean deactivateBuyer(Integer buyerId) {
					logger.info("deactivate request for buyer {} with buyerId{}", buyerId);
					Optional<BuyerEntity> buyerEntity =buyerRepo.findById(buyerId);
					if (buyerEntity.isPresent())
					{
						BuyerEntity buyer=buyerEntity.get();
						buyer.setIsActive(0);
						buyerRepo.save(buyer);
						return true;
						
					}
					return false;
					
				}
		// add product to wishList
				public boolean addTOWishlist(WishlistDTO wishlistDTO) {
					logger.info("add request to wishlist buyerId{},productId{}", wishlistDTO.getBuyerId(),wishlistDTO.getProductId());
					WishlistEntity wishlistEntity=wishlistDTO.createEntity();
					if(wishRepo.save(wishlistEntity).getProdId()!=null) {
						return true;
					}
					return false;
				}
		// remove product from wishList
				public boolean deleteFromWishlist(WishlistDTO wishlistDTO) throws Exception {
					try {
						logger.info("delete request to wishlist buyerId{},productId{}", wishlistDTO.getBuyerId(),wishlistDTO.getProductId());
						WishlistEntity wishlistEntity=wishlistDTO.createEntity();
						wishRepo.delete(wishlistEntity);
						return true;
					}
					catch(Exception e) {
						throw new Exception("Error_DELETEWISHLIST");
					}
					
				}	
		// add product to cart
				public boolean addTOCart(CartDTO cartDTO) {
					logger.info("add request to cart buyerId{},productId{}", cartDTO.getBuyerId(),cartDTO.getProductId());
					CartEntity cartEntity=cartDTO.createEntity();
					
					if(cartRepo.save(cartEntity).getProductId()!=null) {
						return true;
					}
					return false;
				}
		// remove product from cart
		public boolean removeFromCart(CartDTO cartDTO) throws Exception {
			try {
				logger.info("delete request to cart buyerId{},productId{}", cartDTO.getBuyerId(),cartDTO.getProductId());
				CartEntity cartEntity=cartDTO.createEntity();
				cartRepo.delete(cartEntity);
				return true;
			}
			catch(Exception e) {
				throw new Exception("Error_DELETECART");
			}
		}
		//delete whole cart for buyer
		public void deleteCart(Integer buyerId) {
			List<CartEntity> delCart=cartRepo.findByBuyerId(buyerId);
			cartRepo.deleteAll(delCart);
		}
		//fetch products from cart
		public List<CartDTO> fetchCartDetails(Integer buyerId){
			logger.info("delete request to cart buyerId{}", buyerId);
			List<CartEntity> cartEntities=cartRepo.findAll();
			List<CartDTO> cartDTOs=new ArrayList<CartDTO>();
			for(CartEntity cartEntity:cartEntities) {
				if(cartEntity.getBuyerId()==buyerId) {
					CartDTO cartDTO =CartDTO.valueOf(cartEntity);
					cartDTOs.add(cartDTO);
				}
			}
			return cartDTOs;

		}
 	}
