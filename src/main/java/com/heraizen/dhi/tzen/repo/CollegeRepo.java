package com.heraizen.dhi.tzen.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.heraizen.dhi.tzen.domain.College;

public interface CollegeRepo extends MongoRepository<College,String> {

	

}
