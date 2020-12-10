package com.user.UserServiceMs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.UserServiceMs.entity.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, Integer>{
	List<CartEntity> findByBuyerId(Integer buyerId);
}
