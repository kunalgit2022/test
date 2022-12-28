package com.example.demo.customException;

public class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 public UserNotFoundException(String str) {
		super(str);
	}

}
