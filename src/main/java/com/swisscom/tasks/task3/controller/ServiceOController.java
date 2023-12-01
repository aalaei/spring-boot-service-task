package com.swisscom.tasks.task3.controller;

import com.swisscom.tasks.task3.exception.ServiceOServiceException;
import com.swisscom.tasks.task3.model.HttpResponse;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.service.ServiceOService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static java.time.LocalDate.now;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * Controller for {@link ServiceO} entity. It exposes all CRUD operations on {@link ServiceO} entity.
 * Http response is {@link HttpResponse} object.
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/service")
@Tag(name = "Service", description = "Service API")
public class ServiceOController {
    private final ServiceOService serviceOService;
    /**
     * Saves a given service.
     *
     * @param serviceO must not be {@literal null}.
     * @return the saved service will never be {@literal null}. It returns {@link HttpResponse} object.
     * @throws ServiceOServiceException if service with same id already exists.
    */
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
                            description = "Method Not Allowed",
                            responseCode = "405"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<HttpResponse> save(@RequestBody ServiceO serviceO) {
        try {
            ServiceO serviceONew = serviceOService.save(serviceO);
            return ResponseEntity.created(getUri()).body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("OK")
                            .data(Map.of("service", serviceONew))
                            .status(HttpStatus.CREATED)
                            .statusCode(HttpStatus.CREATED.value())
                            .build()
            );
        }catch (ServiceOServiceException serviceOServiceException){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Exists Before")
                            .status(HttpStatus.METHOD_NOT_ALLOWED)
                            .statusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                            .build()
            );
        }
    }
    /**
     * Retrieves all services.
     *
     * @return all services. It returns {@link HttpResponse} object.
    */
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
    public ResponseEntity<HttpResponse> getAll() {
        List<ServiceO> serviceObjects= serviceOService.getAll();
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("OK")
                        .data(Map.of("services", serviceObjects))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    /**
     * Retrieves a service by its id.
     *
     * @param id must not be {@literal null}.
     * @return the service with the given id or {@literal Optional#empty()} if none found. It returns {@link HttpResponse} object.
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
    */
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
    public ResponseEntity<HttpResponse> getById(@PathVariable("ID") String id) {
        Optional<ServiceO> serviceO= serviceOService.getById(id);
        if(serviceO.isPresent())
        {
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("OK")
                            .data(Map.of("service", serviceO))
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Not Found")
                        .data(Map.of("service", serviceO))
                        .status(HttpStatus.NOT_FOUND)
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .build()
        );
    }
    /**
     * Updates the service with the given id.
     *
     * @param id must not be {@literal null}.
     * @param serviceO must not be {@literal null}.
     * @return {@literal true} if service was updated, {@literal false} otherwise. It returns {@link HttpResponse} object.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}.
    */
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
    @PutMapping("/{ID}")
    //TODO: Change Service Objects to DTO
    public ResponseEntity<HttpResponse> updateById(@PathVariable("ID") String id, @RequestBody ServiceO serviceO) {
        try {
            serviceOService.updateById(id, serviceO);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .message("Updated")
                            .timeStamp(now().toString())
                            .statusCode(HttpStatus.OK.value())
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (ServiceOServiceException serviceOServiceException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    HttpResponse.builder()
                            .message("Not Found")
                            .timeStamp(now().toString())
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .status(HttpStatus.NOT_FOUND)
                            .build()
            );
        } catch (Exception e){
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .message("Failed to update")
                            .timeStamp(now().toString())
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }
    }
    /**
     * Deletes the service with the given id.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if service was deleted, {@literal false} otherwise. It returns {@link HttpResponse} object.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}.
    */
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
                            description =  "Method NOT Allowed. Not able to delete",
                            responseCode = "405"
                    ),
                    @ApiResponse(
                            description =  "Not Found. No service exists with this ID",
                            responseCode = "404"
                    )
            }
    )
    @DeleteMapping("/{ID}")
    public ResponseEntity<HttpResponse> deleteById(@PathVariable("ID") String id) {
        try {
            boolean is_deleted = serviceOService.deleteById(id);
            if (is_deleted) {
                return ResponseEntity.ok().body(
                        HttpResponse.builder()
                                .message("Deleted")
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .timeStamp(now().toString())
                                .build()
                );
            }
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                    HttpResponse.builder()
                            .message("Failed to Delete")
                            .status(HttpStatus.METHOD_NOT_ALLOWED)
                            .statusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                            .timeStamp(now().toString())
                            .build()
            );
        }catch (ServiceOServiceException serviceOServiceException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    HttpResponse.builder()
                            .message("Not Found")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .timeStamp(now().toString())
                            .build()
            );
        }
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userid>").toUriString());
    }
}
