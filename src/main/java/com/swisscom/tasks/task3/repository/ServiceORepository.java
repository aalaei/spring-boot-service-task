package com.swisscom.tasks.task3.repository;

import com.swisscom.tasks.task3.model.ServiceO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceORepository extends MongoRepository<ServiceO, String> {
}
