package com.adobe.prj.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.adobe.prj.dao.OrderDao;
import com.adobe.prj.dao.ProductDao;
import com.adobe.prj.entity.Item;
import com.adobe.prj.entity.Order;
import com.adobe.prj.entity.Product;


@Service
public class OrderService {
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private OrderDao orderDao;
	/* 
		{
			"customer":  {
				"email": "sam@adobe.com"
			},
			"items" : [
					{"qty": 3, "product": {"id":5}},
				 	{"qty": 1, "product": {"id":1}}
			]	
		}
	 */
	@Transactional
	public Order placeOrder(Order order) {
		List<Item> items = order.getItems();
		double total = 0.0;
		for(Item item : items) {
			Product p = productDao.findById(item.getProduct().getId()).get();
			if(p.getQuantity() < item.getQty()) {
				throw new IllegalArgumentException("product " + p.getId() + " not in stock");
			}
			p.setQuantity(p.getQuantity() - item.getQty()); // Dirty checking ==> update happens
			item.setPrice(p.getPrice() * item.getQty());
			total += item.getPrice();
		}
		order.setTotal(total);
		return orderDao.save(order); // takes care of saving items in order ==> Cascade
	}
	
	public List<Order> getOrders() {
		return orderDao.findAll();
	}
	
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
