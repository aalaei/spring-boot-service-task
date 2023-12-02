package com.swisscom.tasks.task3.controller;

import com.swisscom.tasks.task3.dto.mapper.DTOMapper;
import com.swisscom.tasks.task3.dto.service.ServiceODTODefault;
import com.swisscom.tasks.task3.dto.service.ServiceODTONoID;
import com.swisscom.tasks.task3.model.Owner;
import com.swisscom.tasks.task3.model.Resource;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.service.OwnerService;
import com.swisscom.tasks.task3.service.ResourceService;
import com.swisscom.tasks.task3.service.ServiceOService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequiredArgsConstructor
public class ServiceOGraphQLController {
    private final ServiceOService serviceOService;
    private final ResourceService resourceService;
    private final OwnerService ownerService;
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
    Page<ServiceO> servicesPaged(@Argument int page, @Argument int size){
        PageRequest pr= PageRequest.of(page, size);
        return serviceOService.getAllPaged(pr);
    }

    /**
     * Returns a service by id.
     * @param id - id of the service.
     * @return a service by id.
     */
    @QueryMapping("service")
    Optional<ServiceO> serviceById(@Argument String id) {
        return serviceOService.getById(id);
    }

    /**
     * Returns a resource by id.
     * @param id - id of the resource.
     * @return a resource by id.
     */
    @QueryMapping
    Optional<Resource> resource(@Argument String id){
        return  resourceService.getById(id);
    }
    /**
     * Returns all resources.
     * @return all resources.
     */
    @QueryMapping
    List<Resource> resources(){
        return resourceService.getAll();
    }
    /**
     * Returns an owner by id.
     * @return all owners in Pages.
     */
    @QueryMapping
    Optional<Owner> owner(@Argument String id){
        return  ownerService.getById(id);
    }
    /**
     * Returns all owners.
     * @return all owners.
     */
    @QueryMapping
    List<Owner> owners(){
        return ownerService.getAll();
    }
    /**
     * Created and Returns a service
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
     * Created and Returns a resource
     * @param resource - resource Input object.
     * @return Created resource.
     */
    @MutationMapping
    Resource createResource(@Argument ResourceInput resource) {
        Resource resourceO = dtoMapper.map(resource, Resource.class);
        resourceService.create(resourceO);
        return resourceO;
    }
    /**
     * Created and Returns an owner
     * @param owner - owner Input object.
     * @return Created owner.
     */
    @MutationMapping
    Owner createOwner(@Argument OwnerInput owner) {
        Owner ownerO = dtoMapper.map(owner, Owner.class);
        ownerService.create(ownerO);
        return ownerO;
    }

    /**
     * Updates a service
     * @param id - id of the service.
     * @param service - service Input object.
     * @return updated version of the service.
     */
    @MutationMapping
    ServiceO updateService(@Argument String id,  @Argument ServiceInput service) {
        ServiceO serviceO = dtoMapper.map(service, ServiceO.class);
        serviceOService.updateById(id, serviceO);
        return serviceO;
    }
    /**
     * Updates a resource
     * @param id - id of the resource.
     * @param resource - resource Input object.
     * @return updated version of the resource.
     */
    @MutationMapping
    Resource updateResource(@Argument String id,  @Argument ResourceInput resource) {
        Resource resourceO = dtoMapper.map(resource, Resource.class);
        resourceService.updateById(id, resourceO);
        return resourceO;
    }
    /**
     * Updates an owner
     * @param id - id of the owner.
     * @param owner - owner Input object.
     * @return updated version of the owner.
     */
    @MutationMapping
    Owner updateOwner(@Argument String id,  @Argument OwnerInput owner) {
        Owner ownerO = dtoMapper.map(owner, Owner.class);
        ownerService.updateById(id, ownerO);
        return ownerO;
    }
    /**
     * Deletes a service by id.
     * @param id - id of the service.
     * @return deleted service.
     */
    @MutationMapping
    Optional<ServiceO> deleteService(@Argument String id) {
        Optional<ServiceO> oldService= serviceOService.getById(id);
        serviceOService.deleteById(id);
        return oldService;
    }
    /**
     * Deletes a resource by id.
     * @param id - id of the resource.
     * @return deleted resource.
     */
    @MutationMapping
    Optional<Resource> deleteResource(@Argument String id) {
        Optional<Resource> oldResource= resourceService.getById(id);
        resourceService.deleteById(id);
        return oldResource;
    }
    /**
     * Deletes an owner by id.
     * @param id - id of the owner.
     * @return deleted owner.
     */
    @MutationMapping
    Optional<Owner> deleteOwner(@Argument String id) {
        Optional<Owner> oldOwner= ownerService.getById(id);
        ownerService.deleteById(id);
        return oldOwner;
    }

    record ServiceInput(String criticalText, List<ResourceInput> resources){}
    record ResourceInput(String criticalText, List<OwnerInput> owners){}
    record OwnerInput(String criticalText, String name, String accountNumber, Integer level){}

}
