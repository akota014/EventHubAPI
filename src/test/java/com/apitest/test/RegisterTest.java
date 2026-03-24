package com.apitest.test;

import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import com.apitest.BaseTest.BaseTest;
import com.apitest.payload.RegisterRequest;
import com.apitest.payload.RegisterResponse;

import static io.restassured.RestAssured.*;


class RegisterTest extends BaseTest {

	@Test
	public void ValidRegistration() {
	    RegisterRequest req = new RegisterRequest();
	    req.email = "akota"+System.currentTimeMillis()+"@test.com";
	    req.password = "secret123";

	   given()
	    	.spec(reqspec)
	    	.body(req)
	    .when()
	    	.post("api/auth/register")
	    .then()
	    	.statusCode(201)
	    	.body("success", equalTo(true))
	        .body("token", notNullValue())
	        .body("user.id", notNullValue())
	        .body("user.email", equalTo(req.email));
	    
	}
	
	@Test
	public void duplicateTest() {
		RegisterRequest req = new RegisterRequest();
		 req.email = "student@example.com";
		    req.password = "secret123";
		    given()
				.spec(reqspec)
		    	.body(req)
		    .when()
		    	.post("api/auth/register")
		    .then()
		    	.statusCode(400)
		    	.body("success", equalTo(false))
		    	.body("error", equalTo("Email already registered"));
	}
	
	@Test
	public void passwordTooShort() {
		RegisterRequest req = new RegisterRequest();
		 req.email = "student@example.com";
		    req.password = "123";
		    given()
				.spec(reqspec)
		    	.body(req)
		    .when()
		    	.post("api/auth/register")
		    .then()
		    	.statusCode(400)
		    	.body("success",equalTo(false))
		    	.body("details[0].message",equalTo("Password must be at least 6 characters"));
		    
	}
	
	@Test
	public void missingFields() {
		
			given()
				.spec(reqspec)
		    	.body("")
		    .when()
		    	.post("api/auth/register")
		    .then()
		    	.statusCode(400)
		    	.body("success", equalTo(false))
				.body("details[0].message", equalTo("A valid email is required"));
	}
	@Test
	public void invalidEmail() {
		RegisterRequest req = new RegisterRequest();
		 req.email = "studentexample";
		   req.password = "secret123";
		   	given()
				.spec(reqspec)
		    	.body(req)
		    .when()
		    	.post("api/auth/register")
		    .then()
		    .body("success", equalTo(false))
			.body("details[0].message", equalTo("A valid email is required"));
	}
	
	@Test
	public void largePayload() {
		RegisterRequest req = new RegisterRequest();
		req.email = "a".repeat(2000)+"@test.com";
	    req.password = "secret123";
	    given()
			.spec(reqspec)
	    	.body(req)
	    .when()
	    	.post("api/auth/register")
	    .then()
	    	.statusCode(500)
	    	.body("error",equalTo("Internal server error"));
	}
	@Test
	public void extraField() {
		String email = "a"+System.currentTimeMillis()+"@test.com";
	    String password = "secret123";
	    String phone = "9899654720";
	    given()
			.spec(reqspec)
	    	.body("{\"email\":\""+email+"\",\"password\":\""+password+ "\",\"phone\":\""+phone+"\"}")
	    .when()
	    	.post("api/auth/register")
	    .then()
	    	.statusCode(201)
	    	.body("success", equalTo(true));
	}
}
