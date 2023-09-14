package com.quest.etna.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.User;
import com.quest.etna.model.DashAttack;
import com.quest.etna.model.DownTilt;
import com.quest.etna.model.UpTilt;
import com.quest.etna.model.ForwardTilt;
import com.quest.etna.repositories.DashAttackRepository;
import com.quest.etna.repositories.DownTiltRepository;
import com.quest.etna.repositories.ForwardTiltRepository;
import com.quest.etna.repositories.UpTiltRepository;
import com.quest.etna.repositories.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)

@RestController
@RequestMapping("")
public class AttacksController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DownTiltRepository downTiltRepository;
	
	@Autowired
	private UpTiltRepository upTiltRepository;
	
	@Autowired 
	private ForwardTiltRepository forwardTiltRepository;
	
	@Autowired 
	private DashAttackRepository dashAttackRepository;
	
	@PutMapping(value = "/downTilt/{downTiltID}", produces = "application/json")
	public ResponseEntity<Object> modifyDownTilt(Authentication authentication, @RequestBody Map<String, Object> dtJson, @PathVariable("downTiltID") Integer downTiltID) {
		
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
	        if (user.isEmpty()) {
	        	Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "ptdr t ki ?");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
	        }
	        if (user.get().getRole().equals("ROLE_ADMIN")) {
	        	DownTilt updateDt = downTiltRepository.findById(downTiltID).get();
	        	
	        	for (Map.Entry<String, Object> entry : dtJson.entrySet()) {
			        switch(entry.getKey()){
			        
				        case "name": 
				        	updateDt.setName(entry.getValue().toString());
				            break;
				    
				        case "endlag":
				        	updateDt.setEndlag(((Integer) entry.getValue()).intValue());
				            break;
				            
				        case "frame":
				        	updateDt.setFrame(((Integer) entry.getValue()).intValue());
				            break;
				            
				        case "damage":
				        	updateDt.setDamage(((Integer) entry.getValue()).intValue());
				            break;

				        default:
				            break;
				    }
	        	
	        	}
	        	
	        	return new ResponseEntity<>(downTiltRepository.save(updateDt), HttpStatus.OK);
	        }
	        Map<String, Object> retVal = new HashMap<>();
	        retVal.put("error", "Only a king can edit an attack");
			return new ResponseEntity<>(retVal, HttpStatus.FORBIDDEN);
	        
	    }
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value = "/upTilt/{upTiltID}", produces = "application/json")
	public ResponseEntity<Object> modifyUpTilt(Authentication authentication, @RequestBody Map<String, Object> upJson, @PathVariable("upTiltID") Integer upTiltID) {
		
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
	        if (user.isEmpty()) {
	        	Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "ptdr t ki ?");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
	        }
	        if (user.get().getRole().equals("ROLE_ADMIN")) {
	        	UpTilt updateUp = upTiltRepository.findById(upTiltID).get();
	        	
	        	for (Map.Entry<String, Object> entry : upJson.entrySet()) {
			        switch(entry.getKey()){
			        
				        case "name":  
				        	updateUp.setName(entry.getValue().toString());
				            break;
				    
				        case "endlag":
				        	updateUp.setEndlag(((Integer) entry.getValue()).intValue());
				            break;
				            
				        case "frame":
				        	updateUp.setFrame(((Integer) entry.getValue()).intValue());
				            break;
				            
				        case "damage":
				        	updateUp.setDamage(((Integer) entry.getValue()).intValue());
				            break;

				        default:
				            break;
				    }
	        	
	        	}
	        	
	        	return new ResponseEntity<>(upTiltRepository.save(updateUp), HttpStatus.OK);
	        }
	        Map<String, Object> retVal = new HashMap<>();
	        retVal.put("error", "Only a king can edit an attack");
			return new ResponseEntity<>(retVal, HttpStatus.FORBIDDEN);
	        
	    }
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}	
	
	@PutMapping(value = "/forwardTilt/{forwardTiltID}", produces = "application/json")
	public ResponseEntity<Object> modifyForwardTilt(Authentication authentication, @RequestBody Map<String, Object> forwardJson, @PathVariable("forwardTiltID") Integer forwardTiltID) {
		
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
	        if (user.isEmpty()) {
	        	Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "ptdr t ki ?");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
	        }
	        if (user.get().getRole().equals("ROLE_ADMIN")) {
	        	ForwardTilt updateForward = forwardTiltRepository.findById(forwardTiltID).get();
	        	
	        	for (Map.Entry<String, Object> entry : forwardJson.entrySet()) {
			        switch(entry.getKey()){
			        
				        case "name": 
				        	updateForward.setName(entry.getValue().toString());
				            break;
				    
				        case "endlag":
				        	updateForward.setEndlag(((Integer) entry.getValue()).intValue());
				            break;
				            
				        case "frame":
				        	updateForward.setFrame(((Integer) entry.getValue()).intValue());
				            break;
				            
				        case "damage":
				        	updateForward.setDamage(((Integer) entry.getValue()).intValue());
				            break;

				        default:
				            break;
				    }
	        	
	        	}
	        	
	        	return new ResponseEntity<>(forwardTiltRepository.save(updateForward), HttpStatus.OK);
	        }
	        Map<String, Object> retVal = new HashMap<>();
	        retVal.put("error", "Only a king can edit an attack");
			return new ResponseEntity<>(retVal, HttpStatus.FORBIDDEN);
	        
	    }
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}	
	
	@PutMapping(value = "/dashAttack/{dashAttackID}", produces = "application/json")
	public ResponseEntity<Object> modifyDashAttack(Authentication authentication, @RequestBody Map<String, Object> dashAttackJson, @PathVariable("dashAttackID") Integer dashAttackID) {
		
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
	        if (user.isEmpty()) {
	        	Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "ptdr t ki ?");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
	        }
	        if (user.get().getRole().equals("ROLE_ADMIN")) {
	        	DashAttack updateDashAttack = dashAttackRepository.findById(dashAttackID).get();
	        	
	        	for (Map.Entry<String, Object> entry : dashAttackJson.entrySet()) {
			        switch(entry.getKey()){
			        
				        case "name": 
				        	updateDashAttack.setName(entry.getValue().toString());
				            break;
				    
				        case "endlag":
				        	updateDashAttack.setEndlag(((Integer) entry.getValue()).intValue());
				            break;
				            
				        case "frame":
				        	updateDashAttack.setFrame(((Integer) entry.getValue()).intValue());
				            break;
				            
				        case "damage":
				        	updateDashAttack.setDamage(((Integer) entry.getValue()).intValue());
				            break;

				        default:
				            break;
				    }
	        	
	        	}
	        	
	        	return new ResponseEntity<>(dashAttackRepository.save(updateDashAttack), HttpStatus.OK);
	        }
	        Map<String, Object> retVal = new HashMap<>();
	        retVal.put("error", "Only a king can edit an attack");
			return new ResponseEntity<>(retVal, HttpStatus.FORBIDDEN);
	        
	    }
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
