package com.swisscom.tasks.task3client.controller;

import com.swisscom.tasks.task3client.dto.service.ServiceODTODefault;
import com.swisscom.tasks.task3client.exception.HttpCallException;
import com.swisscom.tasks.task3client.model.ServiceO;
import com.swisscom.tasks.task3client.service.JsonPlaceholderService;
import com.swisscom.tasks.task3client.service.ParsedJsonService;
import com.swisscom.tasks.task3client.service.impl.JsonPlaceholderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/services")
public class ServiceOController {
    private final ParsedJsonService parsedJsonService;

    @GetMapping
    ResponseEntity<?> getByID(@RequestParam(value = "id", required = false) String id){
        try {
            if (id == null)
                return ResponseEntity.ok(parsedJsonService.findAllIds());
            else
                return ResponseEntity.ok(parsedJsonService.getByID(id));
        }catch (HttpCallException e){
            return e.getResponseEntity();
        }
    }

    @PostMapping
    ResponseEntity<?> createService(@RequestBody ServiceODTODefault serviceODTO){
        try {
            return ResponseEntity.ok(parsedJsonService.createService(serviceODTO));
        }catch (HttpCallException e){
            return e.getResponseEntity();
        }
    }
    @PutMapping
    ResponseEntity<?> updateService(@RequestParam String id, @RequestBody ServiceODTODefault serviceODTO){
        try{
            return ResponseEntity.ok(parsedJsonService.updateService(id,serviceODTO));
        }catch (HttpCallException e){
            return e.getResponseEntity();
        }
    }
    @DeleteMapping
    ResponseEntity<?> deleteService(@RequestParam String id){
        try {
            parsedJsonService.deleteService(id);
            return ResponseEntity.ok("Service ("+id+") Was Deleted Successfully");
        }catch (HttpCallException e){
            return e.getResponseEntity();
        }
    }
}
