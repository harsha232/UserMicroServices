package com.user.UserServiceMs.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.user.UserServiceMs.dto.BuyerDTO;
import com.user.UserServiceMs.dto.BuyerLoginDTO;
import com.user.UserServiceMs.dto.CartDTO;

import com.user.UserServiceMs.dto.SellerDTO;
import com.user.UserServiceMs.dto.SellerLoginDTO;

import com.user.UserServiceMs.dto.WishlistDTO;
import com.user.UserServiceMs.service.UserServiceMSservice;

//controller
@RestController
@CrossOrigin
public class UserServiceController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	UserServiceMSservice userService;
	
	@Autowired
	private Environment environment;
	
	// Create a new buyer
		@PostMapping(value = "/buyer/register",  consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<String> createBuyer(@RequestBody BuyerDTO buyerDTO) throws Exception{
			logger.info("Creation request for buyer {}", buyerDTO);
			try {
				
				if(userService.findPhoneNumber(buyerDTO.getPhoneNo().substring(4))) {
					String errorMessage=environment.getProperty("ALREADY_EXISTS");
					return new ResponseEntity<String>(errorMessage,HttpStatus.ALREADY_REPORTED);
				}
				Integer buyerId=userService.createBuyer(buyerDTO);
				String successMessage=environment.getProperty("BUYER_SUCCESS")+" : "+buyerId;
				return new ResponseEntity<String>(successMessage,HttpStatus.CREATED);
			}
			catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,environment.getProperty(e.getMessage()),e);
			}
		}
		
		// Login for buyer
		@PostMapping(value = "/buyer/login",consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Boolean> login(@RequestBody BuyerLoginDTO buyerLoginDTO) {
			try {
				logger.info("Login request for buyer {} with password {}", buyerLoginDTO.getEmail(),buyerLoginDTO.getPassword());
				Boolean check=userService.buyerLogin(buyerLoginDTO);
				return new ResponseEntity<Boolean>(check,HttpStatus.NOT_FOUND);
			}
			catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,environment.getProperty(e.getMessage()),e);
			}
		}
		// finding buyer
				@GetMapping(value = "/buyer/{buyerId}",produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<Boolean> findBuyer(@PathVariable Integer buyerId) {
					try {
						logger.info("find request for buyer {} with buyerId{}",buyerId);
						Boolean check=userService.findBuyerId(buyerId);
						return new ResponseEntity<Boolean>(check,HttpStatus.NOT_FOUND);
					}
					catch(Exception e) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST,environment.getProperty(e.getMessage()),e);
					}
				}
		
				//deactivate buyer
				@DeleteMapping(value = "/buyer/deactivate/{buyerId}")
			    public ResponseEntity<Boolean> deactivateBuyer(@PathVariable Integer buyerId) {
					try {
						logger.info("deactivate request for buyer {} with buyerId{}",buyerId);
						Boolean check=userService.deactivateBuyer(buyerId);
						return new ResponseEntity<Boolean>(check,HttpStatus.FOUND);
					}
					catch(Exception e) {
						throw new ResponseStatusException(HttpStatus.NOT_FOUND,environment.getProperty(e.getMessage()),e);
					}
			        
			    }
				// activate buyer
				@PostMapping(value = "/buyer/activate",consumes = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<Boolean>buyerActivate(@RequestBody BuyerLoginDTO buyerLoginDTO) {
					try {
						logger.info("Activate request for buyer {} with password {}", buyerLoginDTO.getEmail(),buyerLoginDTO.getPassword());
						Boolean check=userService.activateBuyer(buyerLoginDTO);
						return new ResponseEntity<Boolean>(check,HttpStatus.OK);
					}
					catch(Exception e) {
						throw new ResponseStatusException(HttpStatus.NOT_MODIFIED,environment.getProperty(e.getMessage()),e);
					}
				}
		// Create a new seller
				@PostMapping(value = "/seller/register",  consumes = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<String> createSeller(@RequestBody SellerDTO sellerDTO) throws Exception{
					logger.info("Creation request for seller {}", sellerDTO);
					try {
						if(userService.findPhoneNumberForSeller(sellerDTO.getsPhoneNo().substring(4))) {
							String errorMessage=environment.getProperty("ALREADY_EXISTS");
							return new ResponseEntity<String>(errorMessage,HttpStatus.ALREADY_REPORTED);
						}
						Integer sellerId=userService.createSeller(sellerDTO);
						String successMessage=environment.getProperty("Seller_SUCCESS")+" :"+sellerId;
						return new ResponseEntity<String>(successMessage,HttpStatus.CREATED);
					}
					catch(Exception e) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST,environment.getProperty(e.getMessage()),e);
					}
				}
				// Login seller
				@PostMapping(value = "/seller/login",consumes = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<Boolean> sellerlogin(@RequestBody SellerLoginDTO sellerLoginDTO) {
					try {
						logger.info("Login request for seller {} with password {}", sellerLoginDTO.getsEmail(),sellerLoginDTO.getsPassword());
						Boolean check=userService.sellerLogin(sellerLoginDTO);
						return new ResponseEntity<Boolean>(check,HttpStatus.NOT_FOUND);
					}
					catch(Exception e) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST,environment.getProperty(e.getMessage()),e);
					}
				}
				// finding seller
				@GetMapping(value = "/seller/{sellerId}",produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<Boolean> findSeller(@PathVariable Integer sellerId) {
					try {
						logger.info("find request for seller {} with sellerId{}",sellerId);
						Boolean check=userService.findSellerId(sellerId);
						return new ResponseEntity<Boolean>(check,HttpStatus.FOUND);
					}
					catch(Exception e) {
						throw new ResponseStatusException(HttpStatus.NOT_FOUND,environment.getProperty(e.getMessage()),e);
					}
				}
				//deactivate seller
				@DeleteMapping(value = "/seller/deactivate/{sellerId}")
			    public ResponseEntity<Boolean> deactivateSeller(@PathVariable Integer sellerId) {
					try {
						logger.info("deactivate request for seller {} with sellerId{}",sellerId);
						Boolean check=userService.deactivateSeller(sellerId);
						return new ResponseEntity<Boolean>(check,HttpStatus.FOUND);
					}
					catch(Exception e) {
						throw new ResponseStatusException(HttpStatus.NOT_FOUND,environment.getProperty(e.getMessage()),e);
					}
			        
			    }
				// activate seller
				@PostMapping(value = "/seller/activate",consumes = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<Boolean> sellerActivate(@RequestBody SellerLoginDTO sellerLoginDTO) {
					try {
						logger.info("Activate request for seller {} with password {}", sellerLoginDTO.getsEmail(),sellerLoginDTO.getsPassword());
						Boolean check=userService.activateSeller(sellerLoginDTO);
						return new ResponseEntity<Boolean>(check,HttpStatus.OK);
					}
					catch(Exception e) {
						throw new ResponseStatusException(HttpStatus.NOT_MODIFIED,environment.getProperty(e.getMessage()),e);
					}
				}
				//add product to wishlist
				@PostMapping(value = "/product/addwishlist",consumes = MediaType.APPLICATION_JSON_VALUE)
				public String addProductToWishlist(@RequestBody WishlistDTO wishlistDTO) {
					
						
						String message;
						if(userService.addTOWishlist(wishlistDTO)) {
								message=environment.getProperty("WISH_SUCCESS");
								return message;
						}
						else {
								message=environment.getProperty("WISH_FAIL")+wishlistDTO.getBuyerId();
								return message;
						}
					
				}
//		remove product from wishlist
				@PostMapping(value = "/product/removewishlist")
			    public String deleteWishlist(@RequestBody WishlistDTO wishlistDTO) throws Exception{
					try {
					String message=null;
					if(userService.deleteFromWishlist(wishlistDTO)==true) {
							message=environment.getProperty("REMOVE_WISHLISTSUCCESS");
							
					}
					return message;
					}catch(Exception e) {
						throw new Exception(e.getMessage());
					}
			        
			    }
			//add product to cart
			@PostMapping(value = "/product/addcart",consumes = MediaType.APPLICATION_JSON_VALUE)
			public String addProductToCart(@RequestBody CartDTO cartDTO) {
					String message =null;
					if(userService.addTOCart(cartDTO)) {
								message=environment.getProperty("CART_SUCCESS");
					}
					else {
								message=environment.getProperty("CART_FAIL")+cartDTO.getProductId();
					}
					return message;		
						
						
					
				}
//			remove product from cart
					@PostMapping(value = "/product/removeCart")
				    public String removeCart(@RequestBody CartDTO cartDTO) throws Exception {
						try {
							String message=null;
							if(userService.removeFromCart(cartDTO)==true) {
									message=environment.getProperty("REMOVE_CARTSUCCESS");
									
							}
							return message;
							}catch(Exception e) {
								throw new Exception(e.getMessage());
							}
			    }
// remove all cart products for buyer
			@DeleteMapping(value = "/deleteCart/{buyerId}")
			public void deleteCart(@PathVariable Integer buyerId) throws Exception {
				try {
					logger.info("request to delete cart for buyer: {}",buyerId);
					userService.deleteCart(buyerId);
					
				}catch(Exception e) {
					throw new Exception(e.getMessage());
				}
				
			}
			//fetch product from cart
			@GetMapping(value = "/product/fetchCart/{buyerId}",produces = MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<CartDTO[]> fetchCart(@PathVariable Integer buyerId){
				logger.info("Fetch product detail from cart with buyer {}",buyerId);
				List<CartDTO> cart=userService.fetchCartDetails(buyerId);
				CartDTO[] carts=new CartDTO[cart.size()];
				for(int i=0;i<carts.length;i++) {
					carts[i]=cart.get(i);
				}
				return new ResponseEntity<CartDTO[]>(carts,HttpStatus.FOUND);
			}

}
