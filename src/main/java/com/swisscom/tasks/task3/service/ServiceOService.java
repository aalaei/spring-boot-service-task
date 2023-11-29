package com.swisscom.tasks.task3.service;

import com.swisscom.tasks.task3.model.ServiceO;

import java.util.List;
import java.util.Optional;

public interface ServiceOService {
    String save(ServiceO service);

    List<ServiceO> getAll();

    Optional<ServiceO> getById(String id);

    boolean deleteById(String id);

    boolean updateById(String id, ServiceO serviceO);
}
