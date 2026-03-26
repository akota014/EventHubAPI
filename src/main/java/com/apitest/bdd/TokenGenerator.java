package com.apitest.bdd;

import com.apitest.payload.LoginRequest;
import com.apitest.payload.LoginResponse;

import static io.restassured.RestAssured.*;

public class TokenGenerator {
	public static String token ;
	
	public static String getToken() {
		LoginRequest req = new LoginRequest();
		req.email = "student@example.com";
		req.password = "secret123";
		
		if(token!=null) {
			return token;
		}
		
		LoginResponse res = given()
								.baseUri("https://api.eventhub.rahulshettyacademy.com/")
								.header("Content-Type","application/json")
								.header("accept","application/json")
								.body(req)
							.when()
								.post("/api/auth/login")
							.then()
								.statusCode(200)
								.extract().as(LoginResponse.class);
		token = res.token;
		return token;
	}
}
