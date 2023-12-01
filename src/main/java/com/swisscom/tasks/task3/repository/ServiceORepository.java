package com.swisscom.tasks.task3.repository;

import com.swisscom.tasks.task3.dto.service.ServiceIdDTO;
import com.swisscom.tasks.task3.model.ServiceO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceORepository extends MongoRepository<ServiceO, String> {
    @Query(value = "{}", fields = "{id : 1}")
    List<ServiceIdDTO> findAllIds();
}
