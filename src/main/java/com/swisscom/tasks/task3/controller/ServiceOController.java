package com.swisscom.tasks.task3.controller;

import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.service.ServiceOService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/service")
public class ServiceOController {
    private final ServiceOService serviceOService;
    @PostMapping
    public String save(@RequestBody ServiceO serviceO) {
        return serviceOService.save(serviceO);
    }
    @GetMapping
    public List<ServiceO> getAll() {
        return serviceOService.getAll();
    }
    @GetMapping("/{ID}")
    public Optional<ServiceO> getById(@PathVariable("ID") String id) {
        return serviceOService.getById(id);
    }
    @PutMapping("/update/{ID}")
    //TODO: Change Service Objects to DTO
    public void updateById(@PathVariable("ID") String id, @RequestBody ServiceO serviceO) {
        serviceOService.updateById(id, serviceO);
    }
    @DeleteMapping("/del/{ID}")
    public void deleteById(@PathVariable("ID") String id) {
        serviceOService.deleteById(id);
    }
}
