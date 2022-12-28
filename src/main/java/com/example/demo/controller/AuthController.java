package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.enums.ERole;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.NewUserRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.request.LoginRequest;
import com.example.demo.request.SignupRequest;
import com.example.demo.response.Brand;
import com.example.demo.response.JsonMessageResponse;
import com.example.demo.response.ResponseHandler;
import com.example.demo.response.UserInfoResponse;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.security.jwt.JwtUtils;

@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/auth/v1")
public class AuthController {

	@Autowired
	NewUserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		//UsernamePasswordAuthenticationToken userAuthObject=new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal(); //here i have done upcasting 
		String genaratedjwtToken = jwtUtils.generateToken(userDetails);
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(new UserInfoResponse(userDetails.getId(), userDetails.getUsername(),
				userDetails.getEmail(), roles, genaratedjwtToken));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			// return ResponseEntity.badRequest().body(new JsonMessageResponse("Error:
			// Username is already taken!"));
			return ResponseHandler.generateResponse("Error: Name is already in use!!", HttpStatus.CONFLICT, null);
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseHandler.generateResponse("Error: Email is already in use!", HttpStatus.CONFLICT, null);
		}
		// Create new user's account
		User user = new User();
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(encoder.encode(signUpRequest.getPassword()));
		user.setUsername(signUpRequest.getUsername());
		// user.setInsertDate(null);
		// user.setRoles(signUpRequest.getRole());
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new JsonMessageResponse("User registered successfully!"));
	}

//	@GetMapping("/getAllUser")
//	private ResponseEntity<?> getAllUser() {
//		try {
//			List<User> result = userRepository.findAll();
//			return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, result);
//		} catch (Exception e) {
//			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
//		}
//	}

	@PostMapping("/openCSVFile")
	private void openCSVFile() throws Exception{
		CSVController newCsv=new CSVController();
		newCsv.buildExcelDocument(null, null, null, null);	
	}
	
	static List<Brand> csvMethod() throws IOException {
		CSVReaderInJava csvread = new CSVReaderInJava();
		List<String[]> readData = csvread.readData();
		List<Brand> newbrnds = new ArrayList<>();
		for (String[] csvReaderInJava : readData) {
			int i = 0;
			Brand newbrnd = new Brand();
			if (csvReaderInJava[i] != null)
				newbrnd.setId(Integer.parseInt(csvReaderInJava[i]));
			if (csvReaderInJava[++i] != null)
				newbrnd.setEmail(csvReaderInJava[i]);
			if (csvReaderInJava[++i] != null)
				newbrnd.setPassword(csvReaderInJava[i]);
			if (csvReaderInJava[++i] != null)
				newbrnd.setName(csvReaderInJava[i]);
			if (csvReaderInJava[++i] != null)
				newbrnd.setDate(csvReaderInJava[i].toString());
			newbrnds.add(newbrnd);
		}
		return newbrnds;
	}
}
