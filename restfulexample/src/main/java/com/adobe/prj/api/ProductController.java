package com.adobe.prj.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.adobe.prj.entity.Product;
import com.adobe.prj.service.NotFoundException;
import com.adobe.prj.service.OrderService;

@RestController
@RequestMapping("api/products")
public class ProductController {
	@Autowired
	private OrderService service;
	
	// based on accept http header convert List<Product> to JSON / XML /...
	// http://localhost:8080/api/products
	// http://localhost:8080/api/products?low=1000&high=50000
	@GetMapping()
	public @ResponseBody List<Product> getProducts(@RequestParam(name ="low", defaultValue = "0.0") double low, 
			@RequestParam(name ="high", defaultValue = "0.0") double high) {
		if(low == 0.0 && high == 0.0) {
			return service.getProducts();
		} else {
			return service.getProductsByRange(low, high);
		}
	}
	
	// http://localhost:8080/api/products/5
	@GetMapping("/{pid}")
	public @ResponseBody  Product  getProduct(@PathVariable("pid") int id) throws NotFoundException {
		return service.getProduct(id);
	}
	
	// http://localhost:8080/api/products 
	// payload contains new product data
	@PostMapping()
	public ResponseEntity<Product> addProduct(@RequestBody @Valid Product p) {
		Product prd = service.saveProduct(p);
		return new  ResponseEntity<>(prd, HttpStatus.CREATED);  // 201
	}
			
}
