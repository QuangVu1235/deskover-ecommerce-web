package com.deskover.reponsitory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.deskover.entity.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long>, JpaSpecificationExecutor<Wishlist> {
	
	@Query(value = "SELECT w FROM Wishlist w "
			+ "WHERE w.user.username = ?1 "
			+ "AND w.product.actived = 1 "
			+ "AND w.actived = 1" ,
			nativeQuery = false)
	List<Wishlist> findWishlist(String user);
	
	@Query(value = "SELECT w FROM Wishlist w "
			+ "WHERE w.user.username = ?1 "
			+ "AND w.product.actived = 1 "
			+ "AND w.actived = 1" ,
			nativeQuery = false)
	Page<Wishlist> findWishlist(String user, Pageable Page);
	
	@Query(value = "SELECT w FROM Wishlist w "
			+ "WHERE w.user.username = ?1 "
			+ "AND w.product.slug = ?2 "
			+ "AND w.product.actived = 1" ,
			nativeQuery = false)
	Wishlist getWishlist(String user, String slug);
}