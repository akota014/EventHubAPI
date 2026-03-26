package com.apitest.payload;

import com.apitest.payload.EventResponse.Data;

public class ApiResponse<T> {
	public Boolean success;
	public String error;
	public T data;
	
	public Pagination pagination;
	public String message;
	
	public Details[] details;
	
	public static class Details{
		public String field;
		public String message;
	}
	public static class Pagination{
		public int total;
		public int page;
		public int limit;
		public int totalPages;
	}
	
}
