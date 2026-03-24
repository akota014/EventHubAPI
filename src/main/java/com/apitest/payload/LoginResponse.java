package com.apitest.payload;

public class LoginResponse {
	public Boolean success;
	public String error;
	public String token;
	public User user;
	
	public static class User{
		public int id;
		public String email;
	}
}
