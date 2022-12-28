package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.customException.DataNotFoundException;
import com.example.demo.customException.UserNotFoundException;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.UserRequest;
import com.example.demo.response.ResponseHandler;

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	UserRepository userRepo;
	
	@PostMapping("/saveUser")
	private ResponseEntity<Object> saveUser(@Valid @RequestBody UserRequest userRqst) throws UserNotFoundException
	{
		boolean existByEmail = userRepo.existsByEmail(userRqst.getEmail());
		if(existByEmail==true) 
		{
			return ResponseHandler.generateResponse("a user with this email already exist!", HttpStatus.CONFLICT, null);
		}
			 UserEntity UserJson=UserEntity.build(0,userRqst.getName(),
												    userRqst.getEmail(),
												    userRqst.getAge(), 
										            userRqst.getAddress());
		UserEntity save = userRepo.save(UserJson);		
		//return ResponseEntity.ok("user saved successfully");//this is a simple string response not json response like msg="user saved successfully"
		return ResponseHandler.generateResponse("Successfully added data!", HttpStatus.OK, UserJson);
	}
	
	
	@GetMapping("/getAllUser")
	//@PreAuthorize("isAuthenticated()")
	private ResponseEntity<?> getAllUser() {		
		try {
			List<UserEntity> result = userRepo.findAll();
			UserEntity firstRecodOfTheTable = result.stream().findFirst().orElseThrow(()->new DataNotFoundException("data are not present"));
            return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, result);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }	
	}

}
