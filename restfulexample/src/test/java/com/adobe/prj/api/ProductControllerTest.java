package com.adobe.prj.api;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.adobe.prj.entity.Product;
import com.adobe.prj.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

	@MockBean
	private OrderService service;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getProductsTest() throws Exception {
		List<Product> products = Arrays.asList(new Product(1, "a", 500.00, "c1"), new Product(2, "b", 1500.00, "c2"));
		// mocking
		when(service.getProducts()).thenReturn(products);
		// https://jsonpath.com/
		// @formatter:off
		mockMvc.perform(get("/api/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("a")))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("b")));
		// @formatter:on

		verify(service, times(1)).getProducts();
	}

	@Test
	public void addProductTest() throws Exception {
		Product p = new Product(0, "b", 1500.00, "c1");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(p); // get JSON for Product p

		// mocking if Product type is passed to service he should return a PK 10
		when(service.saveProduct(Mockito.any(Product.class)))
			.thenReturn(Mockito.any(Product.class));

		mockMvc.perform(post("/api/products")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		verify(service, times(1)).saveProduct(Mockito.any(Product.class));

	}

	@Test
	public void addProductExceptionTest() throws Exception {
		Product p = new Product(0, "", -1500.0, "v1");

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(p); // get JSON for Product p

		mockMvc.perform(post("/api/products")
				.content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errors", hasSize(2)))
				.andExpect(jsonPath("$.errors", hasItem("Name is required")))
				.andExpect(jsonPath("$.errors", hasItem("Price -1500.0 should be more than 10")));

		verifyNoInteractions(service);
	}
}
