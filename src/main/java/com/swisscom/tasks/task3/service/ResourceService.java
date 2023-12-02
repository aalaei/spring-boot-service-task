package com.swisscom.tasks.task3.service;

import com.swisscom.tasks.task3.model.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ResourceService {
    Resource create(Resource resource, String serviceID);

    List<Resource> getAll();

    Optional<Resource> getById(String id);

    boolean deleteById(String id);

    boolean deleteAll();

    Resource updateById(String id, Resource resource, boolean cascade);

    Page<Resource> getAllPaged(PageRequest pr);
}
