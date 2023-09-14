//package com.quest.etna.repositories;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//import com.quest.etna.model.Address;
//
//@Repository
//public interface AddressRepository extends CrudRepository<Address, Object> {
//	
//	@Query("SELECT a FROM Address a WHERE user_id = :userid")
//	Iterable<Address> findAllMine(Integer userid);
//	
//}
