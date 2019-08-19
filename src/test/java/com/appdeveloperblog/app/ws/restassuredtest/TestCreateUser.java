package com.appdeveloperblog.app.ws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

class TestCreateUser {

	private final String RESOURCE_PATH = "/mobile-app-ws/users";
	
	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI="http://localhost";
		RestAssured.port = 8080;
	}

	@Test
	void test() {
		List<Map<String, Object>> addresses = new ArrayList<>();
		Map<String, Object> shippingAddress = new HashMap<>();
		shippingAddress.put("streetName", "R-8");
		shippingAddress.put("city", "Dhaka");
		shippingAddress.put("country", "Bangladesh");
		shippingAddress.put("postalCode", "10101");
		shippingAddress.put("type", "shipping");
		addresses.add(shippingAddress);
		Map<String, Object> billingAddress = new HashMap<>();
		billingAddress.put("streetName", "R-5");
		billingAddress.put("city", "Comilla");
		billingAddress.put("country", "Bangladesh");
		billingAddress.put("postalCode", "1603");
		billingAddress.put("type", "billing");
		addresses.add(billingAddress);
		
		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put("firstName", "Shahadat");
		userDetails.put("lastName", "Hossain");
		userDetails.put("email", "shahadat_sust@yahoo.com");
		userDetails.put("password", "123");
		userDetails.put("addresses", addresses);
		
		Response response = RestAssured
		.given()
			.contentType("application/json")
			.accept("application/json")
			.body(userDetails)
		.when()
			.post(RESOURCE_PATH)
		.then()
			.statusCode(HttpStatus.SC_OK)
			.contentType("application/json")
			.extract()
			.response();
		
		String userId = response.jsonPath().getString("userId");
		assertNotNull(userId);
	}

}
