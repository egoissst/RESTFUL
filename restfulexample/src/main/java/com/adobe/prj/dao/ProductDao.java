package com.adobe.prj.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adobe.prj.entity.Product;

public interface ProductDao extends JpaRepository<Product, Integer>{

}
