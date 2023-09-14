package com.quest.etna.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.quest.etna.model.DashAttack;

@Repository
public interface DashAttackRepository extends CrudRepository<DashAttack, Object> {
	
}
