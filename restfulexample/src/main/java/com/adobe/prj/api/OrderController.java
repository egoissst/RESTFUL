package com.adobe.prj.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adobe.prj.entity.Order;
import com.adobe.prj.service.OrderService;

@RestController
@RequestMapping("api/orders")
public class OrderController {
	@Autowired
	private OrderService service;
	
	@PostMapping()
	@ResponseStatus(value = HttpStatus.CREATED)
	public @ResponseBody Order placeOrder(@RequestBody Order o) {
		return service.placeOrder(o);
	}
	
	@GetMapping()
	public List<Order> getOrders() {
		return service.getOrders();
	}
}
