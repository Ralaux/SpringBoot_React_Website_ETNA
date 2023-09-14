package com.quest.etna.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.quest.etna.model.UpTilt;

@Repository
public interface UpTiltRepository extends CrudRepository<UpTilt, Object> {
	
}
