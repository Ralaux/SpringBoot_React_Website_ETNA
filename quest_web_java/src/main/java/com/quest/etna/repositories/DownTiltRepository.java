package com.quest.etna.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.DownTilt;

@Repository
public interface DownTiltRepository extends CrudRepository<DownTilt, Object> {
	
}
