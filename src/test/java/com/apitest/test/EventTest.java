package com.apitest.test;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.apitest.BaseTest.BaseTest;
import com.apitest.bdd.TokenGenerator;
import com.apitest.payload.ApiResponse;
import com.apitest.payload.EventRequest;
import com.apitest.payload.EventResponse;
import com.apitest.payload.EventResponse.Data;
import io.restassured.common.mapper.TypeRef;

public class EventTest extends BaseTest{
	public static int updateId;
	@Test
	public void listAllEvents() {
		String token = TokenGenerator.getToken();
		
		ApiResponse<List<Data>> res= given()
			.spec(reqspec)
			.header("Authorization","Bearer "+token)
		.when()
			.get("api/events?category=Conference&city=Bangalore&search=summit&page=1&limit=10")
		.then()
			.statusCode(200)
			.extract().as(new TypeRef<ApiResponse<List<EventResponse.Data>>>() {});
		
		
	}
	
	@Test
	public void failEvents() {
		String token= "";
		
		ApiResponse<List<Data>> res= given()
			.spec(reqspec)
			.header("Authorization","Bearer "+token)
		.when()
			.get("api/events?category=Conference&city=Bangalore&search=summit&page=1&limit=10")
		.then()
			.statusCode(401)
			.extract().as(new TypeRef<ApiResponse<List<EventResponse.Data>>>() {});
		Assert.assertEquals(res.error, "Unauthorized");
		
	}
	
	@Test
	public void expTokenEvents() {
		String token= TokenGenerator.getToken().substring(0, 10);
		
		ApiResponse<List<Data>> res= given()
			.spec(reqspec)
			.header("Authorization","Bearer "+token)
		.when()
			.get("api/events?category=Conference&city=Bangalore&search=summit&page=1&limit=10")
		.then()
			.statusCode(401)
			.extract().as(new TypeRef<ApiResponse<List<EventResponse.Data>>>() {});
		Assert.assertEquals(res.error, "Invalid or expired token");
	}
	
	@Test
	public void createEvents() {
		String token = TokenGenerator.getToken();
		EventRequest req = new EventRequest();
		req.title="Entertainment Summit 2027";
		  req.description="A premier entertainment conference.";
		  req.category="Conference";
		  req.venue="Mumbai International Centre";
		  req.city="Mumbai";
		  req.eventDate="2026-06-15T09:00:00.000Z";
		  req.price=1500;
		  req.totalSeats=500;
		  req.imageUrl="https://example.com/banner.jpg";
		  
		  ApiResponse<EventResponse.Data> res= given()
					.spec(reqspec)
					.header("Authorization","Bearer "+token)
					.body(req)
				.when()
					.post("/api/events")
				.then()
					.statusCode(201)
					.extract().as(new TypeRef<ApiResponse<EventResponse.Data>>() {});
		  Assert.assertTrue(res.success);
		  Assert.assertEquals(res.data.title, "Entertainment Summit 2027");
		  Assert.assertEquals(res.message, "Event created successfully");
		  
		  updateId = res.data.id;
		  
	}
	
	@Test
	public void createEventEmpToken() {
		String token = "";
		EventRequest req = new EventRequest();
		req.title="Tech Summit 2026";
		  req.description="A premier technology conference.";
		  req.category="Conference";
		  req.venue="Bangalore International Centre";
		  req.city="Bangalore";
		  req.eventDate="2026-06-15T09:00:00.000Z";
		  req.price=1500;
		  req.totalSeats=500;
		  req.imageUrl="https://example.com/banner.jpg";
		  
		  ApiResponse<EventResponse.Data> res= given()
					.spec(reqspec)
					.header("Authorization","Bearer "+token)
					.body(req)
				.when()
					.post("/api/events")
				.then()
					.statusCode(401)
					.extract().as(new TypeRef<ApiResponse<EventResponse.Data>>() {});
		  Assert.assertFalse(res.success);
		  Assert.assertEquals(res.error, "Unauthorized");
				
	}
	
	@Test
	public void createEventInalidToken() {
		String token = TokenGenerator.getToken().substring(0, 20);
		EventRequest req = new EventRequest();
		req.title="Tech Summit 2026";
		  req.description="A premier technology conference.";
		  req.category="Conference";
		  req.venue="Bangalore International Centre";
		  req.city="Bangalore";
		  req.eventDate="2026-06-15T09:00:00.000Z";
		  req.price=1500;
		  req.totalSeats=500;
		  req.imageUrl="https://example.com/banner.jpg";
		  
		  ApiResponse<EventResponse.Data> res= given()
					.spec(reqspec)
					.header("Authorization","Bearer "+token)
					.body(req)
				.when()
					.post("/api/events")
				.then()
					.statusCode(401)
					.extract().as(new TypeRef<ApiResponse<EventResponse.Data>>() {});
		  Assert.assertFalse(res.success);
		  Assert.assertEquals(res.error, "Invalid or expired token");
				
	}
	
	@Test
	public void createEventNoBody() {
		String token = TokenGenerator.getToken();
		
		  
		  ApiResponse<EventResponse.Data> res= given()
					.spec(reqspec)
					.header("Authorization","Bearer "+token)
				.when()
					.post("/api/events")
				.then()
					.statusCode(400)
					.extract().as(new TypeRef<ApiResponse<EventResponse.Data>>() {});
		  Assert.assertFalse(res.success);
		  Assert.assertEquals(res.error, "Validation failed");
				
	}
	
	@Test
	public void eventsById() {
		String token = TokenGenerator.getToken();
		
		  int id =1;
		  ApiResponse<EventResponse.Data> res= given()
					.spec(reqspec)
					.header("Authorization","Bearer "+token)
				.when()
					.get("/api/events/"+id)
				.then()
					.statusCode(200)
					.extract().as(new TypeRef<ApiResponse<EventResponse.Data>>() {});
		  
		  Assert.assertTrue(res.success);
		  Assert.assertEquals(res.data.id, id);
	}
	
	@Test
	public void eventsByIdNotFound() {
		String token = TokenGenerator.getToken();
		
		  int id =99;
		  ApiResponse<EventResponse.Data> res= given()
					.spec(reqspec)
					.header("Authorization","Bearer "+token)
				.when()
					.get("/api/events/"+id)
				.then()
					.statusCode(404)
					.extract().as(new TypeRef<ApiResponse<EventResponse.Data>>() {});
		  
		  Assert.assertFalse(res.success);
		  Assert.assertEquals(res.error, "Event with id 99 not found");
	}
	
	@Test(dependsOnMethods="createEvents")
	public void updateEvent() {
		String token = TokenGenerator.getToken();
		EventRequest req = new EventRequest();
		req.title="Tech Summit 2027";
		  req.description="A premier technology conference.";
		  req.category="Conference";
		  req.venue="Delhi International Centre";
		  req.city="Delhi";
		  req.eventDate="2026-06-15T09:00:00.000Z";
		  req.price=1500;
		  req.totalSeats=500;
		  req.imageUrl="https://example.com/banner.jpg";
		  
		  int id =updateId;
		  ApiResponse<EventResponse.Data> res= given()
					.spec(reqspec)
					.header("Authorization","Bearer "+token)
					.body(req)
				.when()
					.put("/api/events/"+id)
				.then()
				.statusCode(200)
					.extract().as(new TypeRef<ApiResponse<EventResponse.Data>>() {});
		  
		  Assert.assertTrue(res.success);
		  Assert.assertEquals(res.data.id, id);
		  
		  Assert.assertEquals(res.data.venue, "Delhi International Centre");
	}
	
	@Test(dependsOnMethods="createEvents")
	public void updateEventEmpBody() {
		String token = TokenGenerator.getToken();
		EventRequest req = new EventRequest();
		
		  int id =updateId;
		  ApiResponse<EventResponse.Data> res= given()
					.spec(reqspec)
					.header("Authorization","Bearer "+token)
					.body(req)
				.when()
					.put("/api/events/"+id)
				.then()
				.statusCode(400)
					.extract().as(new TypeRef<ApiResponse<EventResponse.Data>>() {});
		  
		  Assert.assertFalse(res.success);
		  Assert.assertEquals(res.error, "Validation failed");
		  
	}
	
	@Test(dependsOnMethods="createEvents")
	public void updateEventEmpToken() {
		String token = "";
		EventRequest req = new EventRequest();
		req.title="Tech Summit 2027";
		  req.description="A premier technology conference.";
		  req.category="Conference";
		  req.venue="Delhi International Centre";
		  req.city="Delhi";
		  req.eventDate="2026-06-15T09:00:00.000Z";
		  req.price=1500;
		  req.totalSeats=500;
		  req.imageUrl="https://example.com/banner.jpg";
		  
		  int id =updateId;
		  ApiResponse<EventResponse.Data> res= given()
					.spec(reqspec)
					.header("Authorization","Bearer "+token)
					.body(req)
				.when()
					.put("/api/events/"+id)
				.then()
				.statusCode(401)
					.extract().as(new TypeRef<ApiResponse<EventResponse.Data>>() {});
		  
		  Assert.assertFalse(res.success);
		  Assert.assertEquals(res.error, "Unauthorized");
		  
	}
	
	@Test(dependsOnMethods="createEvents")
	public void updateEventInvalidId() {
		String token = TokenGenerator.getToken();
		EventRequest req = new EventRequest();
		req.title="Tech Summit 2027";
		  req.description="A premier technology conference.";
		  req.category="Conference";
		  req.venue="Delhi International Centre";
		  req.city="Delhi";
		  req.eventDate="2026-06-15T09:00:00.000Z";
		  req.price=1500;
		  req.totalSeats=500;
		  req.imageUrl="https://example.com/banner.jpg";
		  
		  int id =99;
		  ApiResponse<EventResponse.Data> res= given()
					.spec(reqspec)
					.header("Authorization","Bearer "+token)
					.body(req)
				.when()
					.put("/api/events/"+id)
				.then()
				.statusCode(404)
					.extract().as(new TypeRef<ApiResponse<EventResponse.Data>>() {});
		  
		  Assert.assertFalse(res.success);
		  Assert.assertEquals(res.error, "Event with id 99 not found");
		  
	}
	
	@Test
	public void deleteEventinvalidId() {
		String token = TokenGenerator.getToken();
		
		  int id =99;
		  ApiResponse<EventResponse.Data> res= given()
					.spec(reqspec)
					.header("Authorization","Bearer "+token)
				.when()
					.delete("/api/events/"+id)
				.then()
				.statusCode(404)
					.extract().as(new TypeRef<ApiResponse<EventResponse.Data>>() {});
		  
		  Assert.assertFalse(res.success);
		  Assert.assertEquals(res.error, "Event with id 99 not found");
		  
	}
	

	@Test(dependsOnMethods="updateEvent")
	public void deleteEventBydId() {
		String token = TokenGenerator.getToken();
		
		  int id =updateId;
		  ApiResponse<EventResponse.Data> res= given()
					.spec(reqspec)
					.header("Authorization","Bearer "+token)
				.when()
					.delete("/api/events/"+id)
				.then()
				.statusCode(200)
					.extract().as(new TypeRef<ApiResponse<EventResponse.Data>>() {});
		  
		  Assert.assertTrue(res.success);
		  Assert.assertEquals(res.message, "Event deleted successfully");
		  
	}
	
	
	
}