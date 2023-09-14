package com.quest.etna.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Object> {

	@Query("SELECT u FROM User u WHERE username = :username")
	Optional<User> findByUsername(String username);
	
	Optional<User> findById(Integer id);
}
