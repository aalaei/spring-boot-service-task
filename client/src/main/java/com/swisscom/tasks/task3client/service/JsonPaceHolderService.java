package com.swisscom.tasks.task3client.service;

import com.swisscom.tasks.task3client.dto.service.ServiceODTODefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

public interface JsonPaceHolderService {
    @GetExchange("/services")
    ResponseEntity<?> findAllIds();

    @GetExchange("/services")
    ResponseEntity<?> getByID(@RequestParam String id);

    @PostExchange("/services")
    ResponseEntity<?> createService(@RequestBody ServiceODTODefault serviceODTO);

    @PutExchange("/services")
    ResponseEntity<?> updateService(@RequestParam String id, @RequestBody ServiceODTODefault serviceODTO);

    @DeleteExchange("/services")
    ResponseEntity<?> deleteService(@RequestParam String id);
}
