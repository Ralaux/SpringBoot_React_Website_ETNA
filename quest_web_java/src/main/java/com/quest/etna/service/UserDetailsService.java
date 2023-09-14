package com.quest.etna.service;

import java.util.Optional;

import com.quest.etna.model.JwtUserDetails;

public interface UserDetailsService<T> {

	public Optional<JwtUserDetails> loadUserByUsername(String username);
	
}
