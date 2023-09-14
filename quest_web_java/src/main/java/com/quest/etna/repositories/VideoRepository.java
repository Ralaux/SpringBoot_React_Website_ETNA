package com.quest.etna.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.quest.etna.model.Video;

@Repository
public interface VideoRepository extends CrudRepository<Video, Object> {
	
}
