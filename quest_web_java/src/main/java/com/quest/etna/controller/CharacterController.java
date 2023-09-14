package com.quest.etna.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.User;
import com.quest.etna.model.Character;
import com.quest.etna.model.DownTilt;
import com.quest.etna.repositories.CharacterRepository;
import com.quest.etna.repositories.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)

@RestController
@RequestMapping("/character")
public class CharacterController {

	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private CharacterRepository characterRepository;
	
	@GetMapping(value = "/{charID}", produces = "application/json")
	public ResponseEntity<Object> getOneChar(@PathVariable("charID") Integer charID) {		
		try {
	        Optional<Character> charac = characterRepository.findById(charID);
	        if (charac.isEmpty()) {
				Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "Character not found - WTF you doin' bro ?");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
			}
	        return new ResponseEntity<>(charac.get(), HttpStatus.OK);
		}
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e);
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "", produces = "application/json")
	public ResponseEntity<Object> createAddress(Authentication authentication, @Valid @RequestBody Character characJson) {
		
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
	        if (user.isEmpty()) {
	        	Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "ptdr t ki ?");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
	        }
	        if (user.get().getRole().equals("ROLE_ADMIN")) {
	        	return new ResponseEntity<>(characterRepository.save(characJson), HttpStatus.CREATED);
	        }
	        Map<String, Object> retVal = new HashMap<>();
	        retVal.put("error", "Only a king can create a new character");
			return new ResponseEntity<>(retVal, HttpStatus.FORBIDDEN);
		}
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "", produces = "application/json")
	public ResponseEntity<Object> getUser(Authentication authentication) {		
		try {
	        Iterable<Character> listCharac = characterRepository.findAll();
	        ArrayList<Object> listCharacRet = new ArrayList<Object>();
	        for (Character rowCharac : listCharac) {
	        	Map<String, Object> retRow = new HashMap<>();
	        	retRow.put("id", rowCharac.getId());
	        	retRow.put("name", rowCharac.getName());
	        	retRow.put("speed", rowCharac.getSpeed());
	        	retRow.put("weight", rowCharac.getWeight());
	        	retRow.put("creation_date", rowCharac.getCreation_date());
	        	retRow.put("updated_date", rowCharac.getUpdated_date());
	        	listCharacRet.add(retRow);
	        }
	        return new ResponseEntity<>(listCharacRet, HttpStatus.OK);
		}
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value = "/{CharacterID}", produces = "application/json")
	public ResponseEntity<Object> modifyCharacter(Authentication authentication, @RequestBody Map<String, Object> dtJson, @PathVariable("CharacterID") Integer CharacterID) {
		
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
	        if (user.isEmpty()) {
	        	Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "ptdr t ki ?");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
	        }
	        if (user.get().getRole().equals("ROLE_ADMIN")) {
	        	Character updateDt = characterRepository.findById(CharacterID).get();
	        	
	        	for (Map.Entry<String, Object> entry : dtJson.entrySet()) {
			        switch(entry.getKey()){
				    
				        case "weight":
				        	updateDt.setWeight(((Integer) entry.getValue()).intValue());
				            break;
				            
				        case "speed":
				        	updateDt.setSpeed(((Double) entry.getValue()).floatValue());

				        default:
				            break;
				    }
	        	
	        	}
	        	
	        	return new ResponseEntity<>(characterRepository.save(updateDt), HttpStatus.OK);
	        }
	        Map<String, Object> retVal = new HashMap<>();
	        retVal.put("error", "Only a king can edit a character");
			return new ResponseEntity<>(retVal, HttpStatus.FORBIDDEN);
	        
	    }
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e);
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
}
