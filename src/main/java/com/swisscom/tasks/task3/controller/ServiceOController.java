package com.swisscom.tasks.task3.controller;

import com.swisscom.tasks.task3.dto.service.ServiceIdDTO;
import com.swisscom.tasks.task3.dto.service.ServiceODTO;
import com.swisscom.tasks.task3.dto.service.ServiceODTODefault;
import com.swisscom.tasks.task3.dto.mapper.DTOMapper;
import com.swisscom.tasks.task3.exception.ServiceOServiceException;
import com.swisscom.tasks.task3.model.HttpResponse;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.service.ServiceOService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
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
@RequestMapping("/api/v1/services")
@Tag(name = "Service", description = "Service API")
public class ServiceOController {
    private final ServiceOService serviceOService;
    private final DTOMapper dtoMapper;

    /**
     * Saves a given service.
     *
     * @param serviceODTO must not be {@literal null}. It is {@link ServiceODTO} object.
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
                            description = "Unauthorized. Invalid token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Method Not Allowed",
                            responseCode = "405"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<HttpResponse> createService(@RequestBody ServiceODTODefault serviceODTO) {
        ServiceO serviceO = dtoMapper.map(serviceODTO, ServiceO.class);
        try {
            ServiceO serviceONew = serviceOService.create(serviceO);
            return ResponseEntity.created(getUri()).body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("OK")
                            .data(Map.of("service", serviceONew))
                            .status(HttpStatus.CREATED)
                            .statusCode(HttpStatus.CREATED.value())
                            .build()
            );
        } catch (ServiceOServiceException serviceOServiceException) {
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
     * Retrieves all services(All the Details).
     *
     * @return all services. It returns {@link HttpResponse} object. Each service is {@link ServiceO}
     */
    @Operation(
            description = "Get All services(With the details)",
            summary = "Services(With the details)",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized. Invalid token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not Found. No services exists",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("all")
    public ResponseEntity<HttpResponse> getAllServicesDetailed() {
        List<ServiceO> serviceObjects = serviceOService.getAllDetailed();
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
     * Retrieves services based on the specified criteria. If the ID is specified, it retrieves a specific service by its ID;
     * otherwise, it retrieves a list of all services with only their IDs.
     *
     * @param id The unique identifier of the service to be retrieved.
     * @return An {@link HttpResponse} object containing either the service with the given ID (if specified) or a list of all services
     * (only IDs). If the ID is specified, the response data object's 'service' field holds an {@link Optional} of {@link ServiceODTO};
     * otherwise, it returns an array of {@link ServiceIdDTO} within the 'services' field.
     */
    @Operation(
            description = "Get a service by ID (If ID is not provided, it returns all services IDs)",
            summary = "Service by ID/All Services IDs",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized. Invalid token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not Found. No service exists with this ID",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<HttpResponse> getByIdOrAllIds(@PathParam("id") String id) {
        if (id == null || id.isEmpty()) {
            List<ServiceIdDTO> serviceObjects = serviceOService.getAllIds();
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
        Optional<ServiceODTODefault> serviceODTO =
                dtoMapper.mapOptional(serviceOService.getById(id), ServiceODTODefault.class);
        if (serviceODTO.isPresent()) {
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("OK")
                            .data(Map.of("service", serviceODTO))
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Not Found")
                        .data(Map.of("service", Optional.empty()))
                        .status(HttpStatus.NOT_FOUND)
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .build()
        );
    }

    /**
     * Updates the service with the given ID.
     *
     * @param id          The unique identifier of the service to be updated. Must not be {@literal null}.
     * @param serviceODTO The updated service data. Must not be {@literal null}.
     * @return An {@link HttpResponse} object containing the updated {@link ServiceODTO} if the service was successfully updated.
     * @throws IllegalArgumentException if the given {@code id} is {@literal null}.
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
                            description = "Unauthorized. Invalid token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not Found. No service exists with this ID",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    )
            }
    )
    @PutMapping
    public ResponseEntity<HttpResponse> updateServiceById(@NotNull @PathParam("ID") String id,
                                                          @NotNull @RequestBody ServiceODTODefault serviceODTO) {
        ServiceO serviceO = dtoMapper.map(serviceODTO, ServiceO.class);
        try {
            ServiceODTODefault newServiceODTO = dtoMapper.map(serviceOService.updateById(id, serviceO), ServiceODTODefault.class);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .message("Updated")
                            .timeStamp(now().toString())
                            .data(Map.of("service", newServiceODTO))
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
        } catch (Exception e) {
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
     * Deletes the service with the given ID.
     *
     * @param id The unique identifier of the service to be deleted. Must not be {@literal null}.
     * @return An {@link HttpResponse} object representing the result of the deletion operation. If the service is successfully deleted,
     * the response status is {@link HttpStatus#OK}. If the service does not exist, the response status is {@link HttpStatus#NOT_FOUND}.
     * If the deletion operation is not allowed, the response status is {@link HttpStatus#METHOD_NOT_ALLOWED}.
     * @throws IllegalArgumentException if the given {@code id} is {@literal null}.
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
                            description = "Unauthorized. Invalid token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Method NOT Allowed. Not able to delete",
                            responseCode = "405"
                    ),
                    @ApiResponse(
                            description = "Not Found. No service exists with this ID",
                            responseCode = "404"
                    )
            }
    )
    @DeleteMapping()
    public ResponseEntity<HttpResponse> deleteServiceById(@NotNull @PathParam("ID") String id) {
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
        } catch (ServiceOServiceException serviceOServiceException) {
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
