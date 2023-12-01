package com.swisscom.tasks.task3.repository;

import com.swisscom.tasks.task3.model.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResourceRepository extends MongoRepository<Resource, String> {

}
