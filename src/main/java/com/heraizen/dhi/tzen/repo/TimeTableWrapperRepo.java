package com.heraizen.dhi.tzen.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.heraizen.dhi.tzen.domain.TimeTableOutputWrapper;

public interface TimeTableWrapperRepo extends MongoRepository<TimeTableOutputWrapper, String> {

}
