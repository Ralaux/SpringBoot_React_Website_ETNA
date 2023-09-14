package com.quest.etna.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Object> {

	@Query("SELECT c FROM Comment c WHERE character_id = :id")
	Iterable<Comment> findByCharID(Integer id);
	
	@Query("SELECT c FROM Comment c WHERE user_id = :id")
	Iterable<Comment> findByUserID(Integer id);
	
}