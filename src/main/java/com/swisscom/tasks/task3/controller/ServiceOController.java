package com.swisscom.tasks.task3.controller;

import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.service.ServiceOService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/service")
@Tag(name = "Service", description = "Service API")
public class ServiceOController {
    private final ServiceOService serviceOService;
    @Operation(
            description = "Register a new service",
            summary = "New Service",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description =  "Unauthorized. Invalid token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description =  "Bad Request",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping
    public String save(@RequestBody ServiceO serviceO) {
        return serviceOService.save(serviceO);
    }
    @Operation(
            description = "Get All services",
            summary = "Services",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description =  "Unauthorized. Invalid token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description =  "Not Found. No services exists",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping
    public List<ServiceO> getAll() {
        return serviceOService.getAll();
    }
    @Operation(
            description = "Get a service by ID",
            summary = "Service by ID",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description =  "Unauthorized. Invalid token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description =  "Not Found. No service exists with this ID",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description =  "Bad Request",
                            responseCode = "400"
                    )
            }
    )
    @GetMapping("/{ID}")
    public Optional<ServiceO> getById(@PathVariable("ID") String id) {
        return serviceOService.getById(id);
    }
    @Operation(
            description = "Update a service by ID",
            summary = "Update Service",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description =  "Unauthorized. Invalid token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description =  "Not Found. No service exists with this ID",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description =  "Bad Request",
                            responseCode = "400"
                    )
            }
    )
    @PutMapping("/update/{ID}")
    //TODO: Change Service Objects to DTO
    public void updateById(@PathVariable("ID") String id, @RequestBody ServiceO serviceO) {
        serviceOService.updateById(id, serviceO);
    }
    @Operation(
            description = "Delete a service by ID",
            summary = "Delete Service",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description =  "Unauthorized. Invalid token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description =  "Not Found. No service exists with this ID",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description =  "Not able to delete",
                            responseCode = "500"
                    )
            }
    )
    @DeleteMapping("/del/{ID}")
    public void deleteById(@PathVariable("ID") String id) {
        serviceOService.deleteById(id);
    }
}
