package com.swisscom.tasks.task3client.controller;

import com.swisscom.tasks.task3client.dto.service.ServiceODTODefault;
import com.swisscom.tasks.task3client.model.ServiceO;
import com.swisscom.tasks.task3client.service.JsonPlaceholderService;
import com.swisscom.tasks.task3client.service.impl.JsonPlaceholderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/services")
public class ServiceOController {
    private final JsonPlaceholderServiceImpl jsonPlaceholderService;

    @GetMapping
    ResponseEntity<?> getByID(@RequestParam(value = "id", required = false) String id){
        if(id==null)
            return ResponseEntity.ok(jsonPlaceholderService.findAllIds());
        else
            return ResponseEntity.ok(jsonPlaceholderService.getByID(id));
    }

    @PostMapping
    ServiceO createService(@RequestBody ServiceODTODefault serviceODTO){
        return jsonPlaceholderService.createService(serviceODTO);
    }
    @PutMapping
    ServiceODTODefault updateService(String id, @RequestBody ServiceODTODefault serviceODTO){
        return jsonPlaceholderService.updateService(id,serviceODTO);
    }
    @DeleteMapping
    void deleteService(String id){
        jsonPlaceholderService.deleteService(id);
    }

}
