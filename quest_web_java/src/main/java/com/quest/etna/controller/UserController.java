package com.quest.etna.controller;

import java.util.Optional;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.User;
import com.quest.etna.model.User.UserRole;
import com.quest.etna.model.UserDetails;
import com.quest.etna.repositories.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(value = "", produces = "application/json")
	public ResponseEntity<Object> getUser(Authentication authentication) {		
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
	        if (user.isEmpty()) {
	        	Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "ptdr t ki ?");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
	        }
	        Iterable<User> listUser = userRepository.findAll();
	        ArrayList<Map<String, Object>> listUserRet = new ArrayList<Map<String, Object>>();
	        for (User rowUser : listUser) {
	        	Map<String, Object> userToAdd = new HashMap<>();
	        	userToAdd.put("role", rowUser.getRole());
	        	userToAdd.put("username", rowUser.getUsername());
	        	userToAdd.put("id", rowUser.getId());
	        	listUserRet.add(userToAdd);
	        }
	        return new ResponseEntity<>(listUserRet, HttpStatus.OK);
		}
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/{userID}", produces = "application/json")
	public ResponseEntity<Object> getOneUser(Authentication authentication, @PathVariable("userID") Integer userID) {		
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
	        Optional<User> getUser = userRepository.findById(userID);
	        if (user.isEmpty() || getUser.isEmpty()) {
				Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "Nothing to see here");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
			}
	        UserDetails userDet = new UserDetails(getUser.get().getUsername(), getUser.get().getRole()); 
	        return new ResponseEntity<>(userDet, HttpStatus.OK);
		}
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value = "/{userID}", produces = "application/json")
	public ResponseEntity<Object> updateUser(Authentication authentication, @RequestBody Map<String, String> userJson, @PathVariable("userID") Integer userID) throws Exception {
		
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
			if (userRepository.findById(userID).isEmpty()) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "This user does not exist");
		        error.put("status", HttpStatus.NOT_FOUND.value());
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}
			if (user.get().getRole().equals(UserRole.ROLE_USER.toString()) && user.get().getId() != userID ) { 
				Map<String, Object> error = new HashMap<>();
				error.put("error", "This is not your account and you are not admin: REJECTED PEASANT");
		        error.put("status", HttpStatus.FORBIDDEN.value());
		        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	        }

			for (Map.Entry<String, String> entry : userJson.entrySet()) {
		        switch(entry.getKey()){
		        
			        case "username": 
			        	user.get().setUsername(entry.getValue());
			            break;
			    
			        case "role":
			        	if (user.get().getRole().equals(UserRole.ROLE_USER.toString()) && entry.getValue().equals(UserRole.ROLE_ADMIN.toString()) ) {
			        		Map<String, Object> error = new HashMap<>();
							error.put("error", "You can not crown yourself");
					        error.put("status", HttpStatus.FORBIDDEN.value());
					        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
			        	}
			        	user.get().setRole(entry.getValue());
			            break;

			        default:
			            break;
			    }
		    }
			userRepository.save(user.get());
			UserDetails userDet = new UserDetails(user.get().getUsername(), user.get().getRole()); 
			return new ResponseEntity<>(userDet, HttpStatus.OK);
		}
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
		
	}

	@DeleteMapping(value = "/{userID}", produces = "application/json")
	public ResponseEntity<Object> deleteUser(Authentication authentication, @PathVariable("userID") Integer userID) {		
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
			if (user.isEmpty()) {
				Map<String, Object> retVal = new HashMap<>();
				retVal.put("success", false);
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
			}
				
			if (user.get().getRole().equals(UserRole.ROLE_USER.toString()) && user.get().getId() != userID ) {
				Map<String, Object> error = new HashMap<>();
				error.put("success", false);
		        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	        }
			
			userRepository.deleteById(userID);
			Map<String, Object> retVal = new HashMap<>();
			retVal.put("success", true);
			return new ResponseEntity<>(retVal, HttpStatus.OK);
		}
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("success", false);
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
}
