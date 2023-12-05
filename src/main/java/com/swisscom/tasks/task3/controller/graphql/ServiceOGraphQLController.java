package com.swisscom.tasks.task3.controller.graphql;

import com.swisscom.tasks.task3.dto.mapper.DTOMapper;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.service.ServiceOService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

/**
 * GraphQL controller for Service.
 */
@Controller
@RequiredArgsConstructor
public class ServiceOGraphQLController {
    private final ServiceOService serviceOService;
    private final DTOMapper dtoMapper;

    /**
     * Returns all services.
     *
     * @return all services.
     */
    @QueryMapping
    List<ServiceO> services() {
        List<ServiceO> serviceOIterable = serviceOService.getAllDetailed();
//        return serviceOIterable.stream().map(s -> dtoMapper.map(s, ServiceODTODefault.class)).collect(Collectors.toList());
        return serviceOIterable;
    }

    /**
     * Returns all services Page by Page.
     *
     * @return all services in Pages.
     */
    @QueryMapping
    Page<ServiceO> servicesPaged(@Argument int page, @Argument int size) {
        PageRequest pr = PageRequest.of(page, size);
        return serviceOService.getAllPaged(pr);
    }

    /**
     * Returns a service by id.
     *
     * @param id - id of the service.
     * @return a service by id.
     */
    @QueryMapping("service")
    Optional<ServiceO> serviceById(@Argument String id) {
        return serviceOService.getById(id);
    }

    /**
     * Created and Returns a service
     *
     * @param service - service Input object.
     * @return Created service.
     */
    @MutationMapping
    ServiceO createService(@Argument ServiceInput service) {
        ServiceO serviceO = dtoMapper.map(service, ServiceO.class);
        serviceOService.create(serviceO);
        return serviceO;
    }

    /**
     * Updates a service
     *
     * @param id      - id of the service.
     * @param service - service Input object.
     * @return updated version of the service.
     */
    @MutationMapping
    ServiceO updateService(@Argument String id, @Argument ServiceInput service) {
        ServiceO serviceO = dtoMapper.map(service, ServiceO.class);
        serviceOService.updateById(id, serviceO);
        return serviceO;
    }

    /**
     * Deletes a service by id.
     *
     * @param id - id of the service.
     * @return deleted service.
     */
    @MutationMapping
    Optional<ServiceO> deleteService(@Argument String id) {
        Optional<ServiceO> oldService = serviceOService.getById(id);
        serviceOService.deleteById(id);
        return oldService;
    }

    record ServiceInput(String criticalText, List<ResourceInput> resources) {
    }

    record ResourceInput(String criticalText, List<OwnerInput> owners) {
    }

    record OwnerInput(String criticalText, String name, String accountNumber, Integer level) {
    }

}
