package com.adobe.prj;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.adobe.prj.entity.Product;
import com.adobe.prj.service.OrderService;

@SpringBootApplication
public class RestfulexampleApplication implements CommandLineRunner {
	@Autowired
	private OrderService service;
	
	@Autowired
	private RestTemplate template;
	
	@Bean
	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
			return builder.build();
	}
	
	private void getAllProducts() {
		String result = template.getForObject("http://localhost:8080/api/products", String.class);
		System.out.println(result);
		
		System.out.println("************");
		
		ResponseEntity<List<Product>> response = template.exchange("http://localhost:8080/api/products", 
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
		});
		
		List<Product> products = response.getBody();
		for(Product p : products) {
			System.out.println(p);
		}
	}
		
	private void getProduct() {
		ResponseEntity<Product> response = template.getForEntity("http://localhost:8080/api/products/1", Product.class);
		System.out.println(response.getBody().getName());
	}
	
	private void addUsingRestTemplate() {
		Product p = new Product(0,"Tata Sky", 4500.00, "tv");
		ResponseEntity<Product> resp = template.postForEntity("http://localhost:8080/api/products/", p, Product.class);
		System.out.println(resp.getStatusCodeValue());
		System.out.println(resp.getBody().getId());
	}
	
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
		System.out.println("***** Adding *****");
		addUsingRestTemplate();
		System.out.println("*** GET ALL **********");
		getAllProducts();
		System.out.println("***** Get By ID *******");
		getProduct();
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
