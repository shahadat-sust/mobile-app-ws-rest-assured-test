package com.appdeveloperblog.app.ws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UserdWebServiceEndpointTest {

	private final String CONTEXT_PATH = "/mobile-app-ws";
	
	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI="http://localhost";
		RestAssured.port = 8080;
	}

	//*****************************
	// void testUserLogin()
	//****************************
	@Test
	void a() {
		Map<String, Object> loginDetails = new HashMap<>();
		loginDetails.put("email", "shahadat_sust@yahoo.com");
		loginDetails.put("password", "123");
		
		Response response = RestAssured
		.given()
			.contentType("application/json")
			.accept("application/json")
			.body(loginDetails)
		.when()
			.post(CONTEXT_PATH + "/users/login")
		.then()
			.statusCode(HttpStatus.SC_OK)
			.extract()
			.response();
		
		String authorization = response.getHeader("Authorization");
		assertNotNull(authorization);
		String userId = response.getHeader("userId");
		assertNotNull(userId);
	}

}
