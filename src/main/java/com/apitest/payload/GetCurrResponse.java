package com.apitest.payload;

public class GetCurrResponse {
	public Boolean success;
	public String error;
	public  User user;
	
	public static class User{
		public int userId;
		public String email;
		public int iat;
		public int exp;
	}
}
