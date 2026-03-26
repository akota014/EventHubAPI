package com.apitest.payload;

public class EventResponse {
	
	
	public static class Data{
		public int id;
        public String title;
        public String description;
        public String category;
        public String venue;
        public String city;
        public String eventDate;
        public String price;
        public int totalSeats;
        public int availableSeats;
        public String imageUrl;
        public Boolean isStatic;
        public int userId;
        public String createdAt;
        public String updatedAt;
	}
}
