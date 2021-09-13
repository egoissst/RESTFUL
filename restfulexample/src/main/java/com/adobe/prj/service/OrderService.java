package com.adobe.prj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.adobe.prj.dao.ProductDao;
import com.adobe.prj.entity.Product;


@Service
public class OrderService {
	@Autowired
	private ProductDao productDao;
	
	public List<Product> getProducts() {
		return productDao.findAll();
	}
	
	public Product getProduct(int id) throws NotFoundException {
		Optional<Product> opt = productDao.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new NotFoundException("Product with " + id + " doesn't exist!!"); 
	}
	
	public Product saveProduct(Product p) {
		return productDao.save(p);
	}
	
	public List<Product> getProductsByRange(double low, double high) {
		return productDao.getProductsByRange(low, high);
	}
	
	// pagination
	public Page<Product> getByPage(int page , int size) {
		Pageable pageable = PageRequest.of(page, size);
		return productDao.findAll(pageable);
	}
}
