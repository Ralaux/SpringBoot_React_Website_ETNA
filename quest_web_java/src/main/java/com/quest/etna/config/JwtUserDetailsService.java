package com.quest.etna.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.service.UserService;


@Service("userDetailsService")
public class JwtUserDetailsService implements UserDetailsService{
	
	@Autowired
	UserService userService;
	
	@Override
	public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getByUsername(username);
		JwtUserDetails JwtUserDetails = new JwtUserDetails(user);
		return JwtUserDetails;
	}
}
