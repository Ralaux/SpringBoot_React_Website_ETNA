package com.quest.etna.service;

import com.quest.etna.model.User;

public interface iModelService<T> {

	public User getByUsername(String username);
	
}
