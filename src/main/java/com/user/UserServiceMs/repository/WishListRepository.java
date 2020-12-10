package com.user.UserServiceMs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.UserServiceMs.entity.WishlistEntity;


public interface WishListRepository extends JpaRepository<WishlistEntity, Integer>{

}
