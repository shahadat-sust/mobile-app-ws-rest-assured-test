package com.appdeveloperblog.app.ws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UserdWebServiceEndpointTest {

	private final String CONTEXT_PATH = "/mobile-app-ws";
	private final String CONTENT_TYPE_JSON = "application/json";
	private final String EMAIL = "shahadat_sust@yahoo.com";
	
	private static String authorization;
	private static String userId;
	private static List<Map<String, String>> addresses;
	
	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI="http://localhost";
		RestAssured.port = 8080;
	}

	//*****************************
	// Test User Login
	//****************************
	@Test
	void a() {
		Map<String, Object> loginDetails = new HashMap<>();
		loginDetails.put("email", EMAIL);
		loginDetails.put("password", "123");
		
		Response response = RestAssured
		.given()
			.contentType(CONTENT_TYPE_JSON)
			.accept(CONTENT_TYPE_JSON)
			.body(loginDetails)
		.when()
			.post(CONTEXT_PATH + "/users/login")
		.then()
			.statusCode(HttpStatus.SC_OK)
			.extract()
			.response();
		
		authorization = response.getHeader("Authorization");
		assertNotNull(authorization);
		userId = response.getHeader("userId");
		assertNotNull(userId);
	}
	
	//*****************************
	// Test Get User Details
	//****************************
	@Test
	void b() {
		Response response = RestAssured
		.given()
			.pathParam("userId", userId)
			.accept(CONTENT_TYPE_JSON)
			.header("Authorization", authorization)
		.when()
			.get(CONTEXT_PATH + "/users/{userId}")
		.then()
			.contentType(CONTENT_TYPE_JSON)
			.statusCode(HttpStatus.SC_OK)
			.extract()
			.response();
		
		String userPublicId = response.jsonPath().getString("userId");
		assertNotNull(userPublicId);
		String emailAddress = response.jsonPath().getString("email");
		assertNotNull(emailAddress);
		String firstName = response.jsonPath().getString("firstName");
		assertNotNull(firstName);
		String lastName = response.jsonPath().getString("lastName");
		assertNotNull(lastName);
		assertEquals(EMAIL, emailAddress);
		
		addresses = response.jsonPath().getList("addresses");
		assertEquals(2, addresses.size());
		
		for (Map<String, String> address : addresses) {
			String addressId = address.get("addressId");
			assertNotNull(addressId);
			assertEquals(30, addressId.length());
		}
	}
	
	//*****************************
	// Test Update User Details
	//****************************
	@Test
	void c() {
		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put("firstName", "Shahadat 2");
		userDetails.put("lastName", "Hossain 2");
		
		Response response = RestAssured
		.given()
			.pathParam("userId", userId)
			.accept(CONTENT_TYPE_JSON)
			.contentType(CONTENT_TYPE_JSON)
			.header("Authorization", authorization)
			.body(userDetails)
		.when()
			.put(CONTEXT_PATH + "/users/{userId}")
		.then()
			.contentType(CONTENT_TYPE_JSON)
			.statusCode(HttpStatus.SC_OK)
			.extract()
			.response();
		
		String firstName = response.jsonPath().getString("firstName");
		assertNotNull(firstName);
		assertEquals(userDetails.get("firstName"), firstName);
		String lastName = response.jsonPath().getString("lastName");
		assertNotNull(lastName);
		assertEquals(userDetails.get("lastName"), lastName);
		List<Map<String, String>> storedAddresses = response.jsonPath().getList("addresses");
		assertNotNull(storedAddresses);
		assertEquals(addresses.size(), storedAddresses.size());
		
		for (int i = 0; i < addresses.size(); i++) {
			Map<String, String> address = addresses.get(i);
			Map<String, String> storedAddress = storedAddresses.get(i);
			assertEquals(address.get("addressId"), storedAddress.get("addressId"));
			assertEquals(address.get("city"), storedAddress.get("city"));
			assertEquals(address.get("country"), storedAddress.get("country"));
			assertEquals(address.get("postalCode"), storedAddress.get("postalCode"));
			assertEquals(address.get("type"), storedAddress.get("type"));
		}
	}
	
	//*****************************
	// Test Delete User Details
	//****************************
	@Test
	void d() {
		Response response = RestAssured
		.given()
			.pathParam("userId", userId)
			.accept(CONTENT_TYPE_JSON)
			.header("Authorization", authorization)
		.when()
			.delete(CONTEXT_PATH + "/users/{userId}")
		.then()
			.contentType(CONTENT_TYPE_JSON)
			.statusCode(HttpStatus.SC_OK)
			.extract()
			.response();
		
		String operationName = response.jsonPath().getString("operationName");
		assertNotNull(operationName);
		assertEquals("DELETE", operationName);
		
		String operationResult = response.jsonPath().getString("operationResult");
		assertNotNull(operationResult);
		assertEquals("SUCCESS", operationResult);
	}

}
