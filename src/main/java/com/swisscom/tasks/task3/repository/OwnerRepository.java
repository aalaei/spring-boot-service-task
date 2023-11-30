package com.swisscom.tasks.task3.repository;

import com.swisscom.tasks.task3.model.Owner;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OwnerRepository extends MongoRepository<Owner, String> {
}
