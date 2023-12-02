package com.swisscom.tasks.task3.controller;

import com.swisscom.tasks.task3.dto.mapper.DTOMapper;
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

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ResourceGraphQLController {
    private final ResourceService resourceService;
    private final DTOMapper dtoMapper;

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
     * Created and Returns a resource
     * @param resource - resource Input object.
     * @param serviceID - id of the parent service.
     * @return Created resource.
     */
    @MutationMapping
    Resource createResource(@Argument ResourceInput resource, @Argument String serviceID) {
        Resource resourceO = dtoMapper.map(resource, Resource.class);
        return resourceService.create(resourceO, serviceID);
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
    record ResourceInput(String criticalText, List<OwnerInput> owners){}
    record OwnerInput(String criticalText, String name, String accountNumber, Integer level){}
}
