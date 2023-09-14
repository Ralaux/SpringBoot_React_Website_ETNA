package com.quest.etna.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class DefaultController {
	
	@GetMapping("/testSuccess")
	@ResponseStatus(HttpStatus.OK)
	public String testSuccess() {
		return "success";
	}
	
	@GetMapping("/testNotFound")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String testNotFound() {
		return "not found";
	}
	
	@GetMapping("/testError")
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String testError() {
		return "error";
	}

}