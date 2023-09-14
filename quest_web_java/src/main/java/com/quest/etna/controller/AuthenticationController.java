package com.quest.etna.controller;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import com.quest.etna.model.UserDetails;
import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.model.User;
import com.quest.etna.model.User.UserRole;
import com.quest.etna.repositories.UserRepository;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthenticationController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	 private AuthenticationManager authManager;
	
	@Autowired
	JwtTokenUtil jwtUtils;
	
	@Autowired
	@Qualifier("userDetailsService")
	private JwtUserDetailsService userDetailsService;
	
	@GetMapping(value = "/username/{username}", produces = "application/json")
	public ResponseEntity<Object> byUsername(@PathVariable("username") String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@PostMapping(value = "/register", produces = "application/json")
	public ResponseEntity<Object> register(@Valid @RequestBody User userJson) {
		
		try {
			Optional<User> user = userRepository.findByUsername(userJson.getUsername());
			if (user.isPresent()) {
				Map<String, Object> error = new HashMap<>();
		        error.put("error", "I am seeing double bro");
				return new ResponseEntity<>(error, HttpStatus.CONFLICT);
			}
			userJson.setRole(UserRole.ROLE_USER.toString());
			userRepository.save(userJson);
		}
		catch(Exception e) {
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
			
		}
		UserDetails userDetails = new UserDetails();
		userDetails.setUsername(userJson.getUsername());
		userDetails.setRole(userJson.getRole());
		return new ResponseEntity<>(userDetails, HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/authenticate")
	public ResponseEntity<Object> autenticate(@Valid @RequestBody Map<String, Object> credentials) {
		try {
			UsernamePasswordAuthenticationToken token = new 
		
			    UsernamePasswordAuthenticationToken(
			      credentials.get("username"), 
			      credentials.get("password"));
			
			Authentication authResult = authManager.authenticate(token);
			
			SecurityContextHolder.getContext().setAuthentication(authResult);
			String jwt = jwtUtils.generateToken((org.springframework.security.core.userdetails.UserDetails) authResult.getPrincipal());
			
			Map<String, Object> object = new HashMap<>();
			object.put("id", userRepository.findByUsername(credentials.get("username").toString()).get().getId());
			object.put("token", jwt);
			object.put("role", userRepository.findByUsername(credentials.get("username").toString()).get().getRole());
			if(authResult.isAuthenticated()) {
				return new ResponseEntity<>(object, HttpStatus.OK);
			}
		} 
		catch (Exception e){
			return new ResponseEntity<> (e, HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping(value = "/findUser")
	public ResponseEntity<Object> findUser(@Valid @RequestBody Map<String, Object> credentials) {
		try {
			Optional<User> user = userRepository.findByUsername(credentials.get("username").toString());
			if (user.isEmpty()) {
				return new ResponseEntity<> (HttpStatus.NOT_FOUND);
			}
			
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		} 
		catch (Exception e){
			return new ResponseEntity<> (e, HttpStatus.NOT_FOUND);
		}
		
	}
	
	@PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
	@GetMapping(value = "/me")
	public ResponseEntity<Object> me(Authentication authentication) {
	    try {
	        String username = authentication.getName(); // Get username from authentication token
	        Optional<User> user = userRepository.findByUsername(username);
	        UserDetails userRtn = new UserDetails(user.get().getUsername(), user.get().getRole());
	        return new ResponseEntity<>(userRtn, HttpStatus.OK);
	    } catch (Exception e) {
	        Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	    }
	}
}
