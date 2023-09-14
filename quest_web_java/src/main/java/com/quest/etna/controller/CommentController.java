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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;
import com.quest.etna.model.Comment;
import com.quest.etna.repositories.CharacterRepository;
import com.quest.etna.repositories.CommentRepository;
import com.quest.etna.repositories.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)

@RestController
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private CharacterRepository characterRepository;
	
	@PostMapping(value = "", produces = "application/json")
	public ResponseEntity<Object> createComment(Authentication authentication, @Valid @RequestBody Map<String, Object> comJson) {
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
	        if (user.isEmpty()) {
	        	Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "ptdr t ki ?");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
	        }
	        Comment newCom = new Comment();
	        newCom.setBody(comJson.get("body").toString());
	        newCom.setCharacter(characterRepository.findById(comJson.get("character_id")).get());
	        newCom.setUser(user.get());
	        commentRepository.save(newCom);
	        Map<String, Object> retCom = new HashMap<>();
	        retCom.put("id", newCom.getId());
	        retCom.put("body", newCom.getBody());
	        Map<String, Object> charac = new HashMap<>();
        	charac.put("name", newCom.getCharacter().getName());
        	charac.put("id", newCom.getCharacter().getId());
        	retCom.put("character", charac);
	        UserDetails userDet = new UserDetails();
        	userDet.setUsername(newCom.getUser().getUsername());
        	userDet.setRole(newCom.getUser().getRole());
	        retCom.put("user", userDet );
			return new ResponseEntity<>(retCom, HttpStatus.CREATED);
		}
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value = "/{comID}", produces = "application/json")
	public ResponseEntity<Object> modifyComment(Authentication authentication, @Valid @RequestBody Map<String, Object> comJson, @PathVariable("comID") Integer comID) {
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
	        if (user.isEmpty()) {
	        	Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "ptdr t ki ?");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
	        }
	        if (user.get().getUsername().equals(commentRepository.findById(comID).get().getUser().getUsername())) {
	        	Optional<Comment> putCom = commentRepository.findById(comID);
	        	if (putCom.isEmpty()) {
		        	Map<String, Object> retVal = new HashMap<>();
					retVal.put("error", "Comment not found, wtf you doing bro");
					return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
		        }
	        	putCom.get().setBody(comJson.get("body").toString());
	        	commentRepository.save(putCom.get());
	        	Map<String, Object> retCom = new HashMap<>();
	 	        retCom.put("id", putCom.get().getId());
	 	        retCom.put("body", putCom.get().getBody());
	 	        Map<String, Object> charac = new HashMap<>();
	        	charac.put("name", putCom.get().getCharacter().getName());
	        	charac.put("id", putCom.get().getCharacter().getId());
	        	retCom.put("character", charac);
	 	        UserDetails userDet = new UserDetails();
	         	userDet.setUsername(putCom.get().getUser().getUsername());
	         	userDet.setRole(putCom.get().getUser().getRole());
	 	        retCom.put("user", userDet );
	 			return new ResponseEntity<>(retCom, HttpStatus.OK);
	        }
	        Map<String, Object> retVal = new HashMap<>();
			retVal.put("error", "Not yours mon reuf");
			return new ResponseEntity<>(retVal, HttpStatus.FORBIDDEN);
		}
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping(value = "/{comID}", produces = "application/json")
	public ResponseEntity<Object> deleteComment(Authentication authentication, @PathVariable("comID") Integer comID) {
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
	        if (user.isEmpty()) {
	        	Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "ptdr t ki ?");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
	        }
	        if (user.get().getUsername().equals(commentRepository.findById(comID).get().getUser().getUsername()) || user.get().getRole().equals("ROLE_ADMIN")) {
	        	commentRepository.deleteById(comID);
	        	Map<String, Object> retVal = new HashMap<>();
	        	retVal.put("success", true);
	 			return new ResponseEntity<>(retVal, HttpStatus.OK);
	        }
	        Map<String, Object> retVal = new HashMap<>();
			retVal.put("error", "Not yours and not king mon reuf");
			return new ResponseEntity<>(retVal, HttpStatus.FORBIDDEN);
		}
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/{charID}", produces = "application/json")
	public ResponseEntity<Object> getCommentByChar(@PathVariable("charID") Integer charID) {
		try {
			Iterable<Comment> listCom = commentRepository.findByCharID(charID);
	        ArrayList<Map<String, Object>> listComRet = new ArrayList<Map<String, Object>>();
			for (Comment rowCom : listCom) {
	        	Map<String, Object> retRow = new HashMap<>();
	        	retRow.put("id", rowCom.getId());
	        	retRow.put("body", rowCom.getBody());
	        	Map<String, Object> charac = new HashMap<>();
	        	charac.put("name", rowCom.getCharacter().getName());
	        	charac.put("id", rowCom.getCharacter().getId());
	        	retRow.put("character", charac);
	        	UserDetails userDet = new UserDetails();
	        	userDet.setUsername(rowCom.getUser().getUsername());
	        	userDet.setRole(rowCom.getUser().getRole());
	        	retRow.put("user", userDet );
	        	listComRet.add(retRow);
	        }
			return new ResponseEntity<>(listComRet, HttpStatus.OK);
		}
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/mine", produces = "application/json")
	public ResponseEntity<Object> getCommentByUser(Authentication authentication) {
		try {
			String username = authentication.getName();
	        Optional<User> user = userRepository.findByUsername(username);
	        if (user.isEmpty()) {
	        	Map<String, Object> retVal = new HashMap<>();
				retVal.put("error", "ptdr t ki ?");
				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
	        }
			Iterable<Comment> listCom = commentRepository.findByUserID(user.get().getId());
	        ArrayList<Map<String, Object>> listComRet = new ArrayList<Map<String, Object>>();
	        for (Comment rowCom : listCom) {
	        	Map<String, Object> retRow = new HashMap<>();
	        	retRow.put("id", rowCom.getId());
	        	retRow.put("body", rowCom.getBody());
	        	Map<String, Object> charac = new HashMap<>();
	        	charac.put("name", rowCom.getCharacter().getName());
	        	charac.put("id", rowCom.getCharacter().getId());
	        	retRow.put("character", charac);
	        	UserDetails userDet = new UserDetails();
	        	userDet.setUsername(rowCom.getUser().getUsername());
	        	userDet.setRole(rowCom.getUser().getRole());
	        	retRow.put("user", userDet );
	        	listComRet.add(retRow);
	        }
			return new ResponseEntity<>(listComRet, HttpStatus.OK);
		}
		catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
	        error.put("error", e.getCause().getCause().getMessage());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
		
}
