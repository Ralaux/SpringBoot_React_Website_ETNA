package com.quest.etna.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quest.etna.model.User;
import com.quest.etna.repositories.UserRepository;

@Service
@Transactional
public class UserService implements iModelService<User>{
	
	@Autowired
	private UserRepository userRepository;

	public UserService() {}

	@Override
	public User getByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isEmpty()) {
			return null;
		}
		return user.get();
	};
	
}
