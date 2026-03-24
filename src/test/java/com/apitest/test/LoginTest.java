package com.apitest.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.apitest.BaseTest.BaseTest;
import com.apitest.payload.GetCurrResponse;
import com.apitest.payload.LoginRequest;
import com.apitest.payload.LoginResponse;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class LoginTest extends BaseTest {
	
	public static String token;
	@Test
	public void loginSuccess() {
		LoginRequest req = new LoginRequest();
		LoginResponse res = new LoginResponse();
		req.email ="student@example.com";
		req.password = "secret123";
		
		res= given()
			.spec(reqspec)
			.body(req)
		.when()
			.post("api/auth/login")
		.then()
			.statusCode(200)
			.extract().as(LoginResponse.class);
		
		Assert.assertTrue(res.success);
		Assert.assertNotNull(res.token);
		Assert.assertEquals(res.user.email, "student@example.com");
		token =res.token;
	}
	
	@Test
	public void wrongPassword() {
		LoginRequest req = new LoginRequest();
		req.email ="student@example.com";
		req.password = "secret";
		
		given()
			.spec(reqspec)
			.body(req)
		.when()
			.post("api/auth/login")
		.then()
			.statusCode(400)
			.body("success", equalTo(false))
			.body("error",equalTo("Invalid email or password"));
	}
	@Test
	public void invalidUser() {
		LoginRequest req = new LoginRequest();
		req.email ="student"+System.currentTimeMillis()+"@example.com";
		req.password = "secret123";
		System.out.print(System.currentTimeMillis());
		given()
			.spec(reqspec)
			.body(req)
		.when()
			.post("api/auth/login")
		.then()
			.statusCode(400)
			.body("success", equalTo(false))
			.body("error",equalTo("Invalid email or password"));
	}
	
	@Test
	public void missingField() {
		LoginRequest req = new LoginRequest();
		req.email ="";
		req.password = "secret123";
		System.out.print(System.currentTimeMillis());
		given()
			.spec(reqspec)
			.body(req)
		.when()
			.get("api/auth/me")
		.then()
			.statusCode(401)
			.body("success", equalTo(false))
			.body("error",equalTo("Unauthorized"));
	}
	
	@Test(dependsOnMethods="loginSuccess")
	public void getCurrUser() {
		GetCurrResponse res = new GetCurrResponse();
		res = given()
			.spec(reqspec)
			.header("Authorization","Bearer "+token)
		.when()
			.get("api/auth/me")
		.then()
		.statusCode(200)
			.extract().as(GetCurrResponse.class);
//		System.out.println(res.success+""+res.error);
		Assert.assertTrue(res.success);
		Assert.assertEquals(res.user.email, "student@example.com");
	}
	
	public void unauthorisedCurrUer() {
		GetCurrResponse res = new GetCurrResponse();
		res = given()
			.spec(reqspec)
			.header("Authorization","Bearer "+token)
		.when()
			.get("api/auth/me")
		.then()
		.statusCode(200)
			.extract().as(GetCurrResponse.class);
//		System.out.println(res.success+""+res.error);
		Assert.assertFalse(res.success);
		Assert.assertEquals(res.error, "Invalid or expired token");
	}
}
