package com.quest.etna.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.Character;

@Repository
public interface CharacterRepository extends CrudRepository<Character, Object> {

	@Query("SELECT c FROM Character c WHERE name = :charName")
	Optional<Character> findByName(String charName);
	
	Optional<Character> findById(Integer id);
	
}
