package com.adobe.prj.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adobe.prj.entity.Product;

public interface ProductDao extends JpaRepository<Product, Integer> {
	// select * from products where category = ?
	List<Product> findByCategory(String category);
	// select * from products where category = ? and quantity = ?
	List<Product> findByCategoryAndQuantity(String category, int quantity);
	// select * from products where category = ? or quantity = ?
	List<Product> findByCategoryOrQuantity(String category, int quantity);
	
	@Query("from Product where price >= :low and price <= :high")
	List<Product> getProductsByRange(@Param("low") double lower, @Param("high") double higher);
	
	@Modifying
	@Query("update Product set price = :pr where id = :id")
	void updateProductPrice(@Param("pr") double price, @Param("id") int id);
}
