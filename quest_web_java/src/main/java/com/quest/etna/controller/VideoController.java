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
import com.quest.etna.model.Video;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.repositories.VideoRepository;

@CrossOrigin(origins = "*", maxAge = 3600)

@RestController
@RequestMapping("")
public class VideoController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VideoRepository videoRepository;
	
	@PutMapping(value = "/video/{videoID}", produces = "application/json")
	public ResponseEntity<Object> modifyDownTilt(Authentication authentication, @RequestBody Map<String, Object> videoJson, @PathVariable("videoID") Integer videoID) {
		
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
	        if (user.isEmpty()) {
	        	Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "ptdr t ki ?");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
	        }
	        if (user.get().getRole().equals("ROLE_ADMIN")) {
	        	Video updateVideo = videoRepository.findById(videoID).get();
	        	
	        	for (Map.Entry<String, Object> entry : videoJson.entrySet()) {
			        switch(entry.getKey()){
			        
				        case "url": 
				        	updateVideo.setUrl(entry.getValue().toString());
				            break;
				    
				        case "Yt_channel":
				        	updateVideo.setYt_channel(entry.getValue().toString());
				            break;

				        default:
				            break;
				    }
	        	
	        	}
	        	
	        	return new ResponseEntity<>(videoRepository.save(updateVideo), HttpStatus.OK);
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
