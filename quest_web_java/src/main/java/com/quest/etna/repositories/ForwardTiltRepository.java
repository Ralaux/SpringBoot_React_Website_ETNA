package com.quest.etna.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.quest.etna.model.ForwardTilt;

@Repository
public interface ForwardTiltRepository extends CrudRepository<ForwardTilt, Object> {
	
}
