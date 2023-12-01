package com.swisscom.tasks.task3.service;

import com.swisscom.tasks.task3.model.ServiceO;

import java.util.List;
import java.util.Optional;

/**
 * Service for {@link ServiceO} entity.
 */
public interface ServiceOService {
    ServiceO save(ServiceO service);

    List<ServiceO> getAll();

    Optional<ServiceO> getById(String id);

    boolean deleteById(String id);

    boolean updateById(String id, ServiceO serviceO);
}
