package com.swisscom.tasks.task3.service.impl;

import com.swisscom.tasks.task3.exception.ResourceServiceException;
import com.swisscom.tasks.task3.model.Resource;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.repository.ResourceRepository;
import com.swisscom.tasks.task3.repository.ServiceORepository;
import com.swisscom.tasks.task3.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@Slf4j
@CacheConfig(cacheNames = "resource")
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final ServiceORepository serviceORepository;
    @Override
    public Resource create(Resource resource, String serviceID){
        ServiceO parentService= serviceORepository
                .findById(serviceID).orElseThrow(()->
                        new ResourceServiceException("Service with id "+serviceID+" not found"));
        Resource newResource= resourceRepository.save(resource);
        if(parentService.getResources()==null){
            parentService.setResources(List.of(newResource));
        }else{
            parentService.getResources().add(newResource);
        }
        serviceORepository.save(parentService);
        return newResource;
    }

    @Override
    public List<Resource> getAll() {
        return resourceRepository.findAll();
    }

    @Override
    @Cacheable(key = "#id")
    public Optional<Resource> getById(String id) {
        return resourceRepository.findById(id);
    }

    @Override
    @CacheEvict(key = "#id")
    public boolean deleteById(String id) {
        if(!resourceRepository.existsById(id))
            return false;;
        resourceRepository.deleteById(id);
        return true;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean deleteAll() {
        resourceRepository.deleteAll();
        return true;
    }

    @Override
    @CachePut(key = "#id")
    public Resource updateById(String id, Resource resource) {
        if(!resourceRepository.existsById(id))
            return null;
        resource.setId(id);
        return resourceRepository.save(resource);
    }

    @Override
    public Page<Resource> getAllPaged(PageRequest pr) {
        return resourceRepository.findAll(pr);
    }
}
