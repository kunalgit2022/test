package com.example.demo.response;

import java.util.List;

import org.springframework.http.ResponseCookie;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor
public class UserInfoResponse {
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
	private String token;
	private String type = "Bearer";

	public UserInfoResponse(Long id, String username, String email, List<String> roles, String token) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.token = token;
	}

}
