package com.adobe.prj;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.adobe.prj.entity.Product;
import com.adobe.prj.service.OrderService;

@SpringBootApplication
public class RestfulexampleApplication implements CommandLineRunner {
	@Autowired
	private OrderService service;
	
	public static void main(String[] args) {
		SpringApplication.run(RestfulexampleApplication.class, args);
	}
	
	/*
	 * this method gets called once spring container is created
	 */
	@Override
	public void run(String... args) throws Exception {
//		addProduct();
//		getProducts();
	}

	private void getProducts() {
		List<Product> products = service.getProducts();
		for(Product p : products) {
			System.out.println(p);
		}
	}

	private void addProduct() {
		Product p = new Product(0, "iPhone 12", 120000.00, "mobile");
		p.setQuantity(100);
		service.saveProduct(p);
	}

}
