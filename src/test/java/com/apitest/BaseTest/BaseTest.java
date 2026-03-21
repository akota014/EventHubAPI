package com.apitest.BaseTest;

import org.testng.annotations.BeforeMethod;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseTest {
	protected RequestSpecification reqspec;
	@BeforeMethod
	public void spec() {
		
		reqspec = new RequestSpecBuilder()
		.setBaseUri("https://api.eventhub.rahulshettyacademy.com/")
		.addHeader("Content-Type","application/json")
		.addHeader("accept","application/json")
		.build();
	
	}
}
