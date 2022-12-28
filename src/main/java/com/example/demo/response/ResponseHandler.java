package com.example.demo.response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author prasanjit 
 * @see JsonMessageResponse
 * @apiNote custom class for generate response.
 *
 */
public class ResponseHandler {
	
	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);
		map.put("timestamp", new Date());
		//map.put("author", "prasanjit");

		return new ResponseEntity<Object>(map, status);
	}

}
