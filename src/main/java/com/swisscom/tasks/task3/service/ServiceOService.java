package com.swisscom.tasks.task3.service;

import com.swisscom.tasks.task3.dto.service.ServiceIdDTO;
import com.swisscom.tasks.task3.model.ServiceO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * Service for {@link ServiceO} entity.
 */
public interface ServiceOService {
    ServiceO create(ServiceO service);

    List<ServiceIdDTO> getAllIds();

    List<ServiceO> getAllDetailed();

    Optional<ServiceO> getById(String id);

    boolean deleteById(String id);

    boolean deleteAll();

    ServiceO updateById(String id, ServiceO serviceO);

    ServiceO updateById(String id, ServiceO serviceO, boolean cascade);

    Page<ServiceO> getAllPaged(PageRequest pr);
}
