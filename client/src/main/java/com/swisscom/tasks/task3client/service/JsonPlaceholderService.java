package com.swisscom.tasks.task3client.service;

import com.swisscom.tasks.task3client.dto.service.ServiceODTODefault;
import com.swisscom.tasks.task3client.model.ServiceO;
import org.springframework.web.service.annotation.*;

import java.util.List;
import java.util.Optional;

public interface JsonPlaceholderService {
//    @GetExchange("/api/v1/services")
    List<String> findAllIds();
//    @GetExchange("/api/v1/services")
    Optional<ServiceODTODefault> getByID(String id);
//    @PostExchange("/api/v1/services")
    ServiceO createService(ServiceODTODefault serviceODTO);
//    @PutExchange("/api/v1/services")
    ServiceODTODefault updateService(String id, ServiceODTODefault serviceODTO);
//    @DeleteExchange("/api/v1/services")
    void deleteService(String id);
}
