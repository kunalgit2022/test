package com.example.demo.request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupRequest {
	private String username; 
	private String email; 
	private String password;
	private Set<String> role;

}
