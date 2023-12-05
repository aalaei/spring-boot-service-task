package com.swisscom.tasks.task3.service.impl;

import com.swisscom.tasks.task3.crypto.service.ResourceEncryptor;
import com.swisscom.tasks.task3.exception.ResourceServiceException;
import com.swisscom.tasks.task3.model.Resource;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.repository.OwnerRepository;
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
import org.springframework.data.domain.PageImpl;
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
    private final OwnerRepository ownerRepository;
    private final ResourceEncryptor resourceEncryptor;

    /**
     * Saves a given resource. It also adds the resource to the parent service.
     *
     * @param resource  - must not be {@literal null}.
     * @param serviceID - id of the parent service.
     */
    @Override
    public Resource create(Resource resource, String serviceID) {
        resource = resourceEncryptor.decrypt(resource);
        if (resource.getOwners() == null)
            resource.setOwners(List.of());
        ServiceO parentService = serviceORepository
                .findById(serviceID).orElseThrow(() ->
                        new ResourceServiceException("Service with id " + serviceID + " not found"));
        Resource newResource = resourceRepository.save(resource);
        saveCascade(newResource);
        if (parentService.getResources() == null) {
            parentService.setResources(List.of(newResource));
        } else {
            parentService.getResources().add(newResource);
        }
        serviceORepository.save(parentService);
        return resourceEncryptor.encrypt(newResource);
    }

    /**
     * Returns all resources.
     *
     * @return - all resources.
     */
    @Override
    public List<Resource> getAll() {
        return resourceRepository.findAll().stream().map(resourceEncryptor::encrypt).toList();
    }

    /**
     * Returns a resource by id.
     *
     * @param id - id of the resource.
     * @return - a resource by id.
     */
    @Override
    @Cacheable(key = "#id")
    public Optional<Resource> getById(String id) {
        return resourceRepository.findById(id).map(resourceEncryptor::encrypt);
    }

    /**
     * Deletes a given resource. It also deletes all owners of this resource.
     *
     * @param resource - must not be {@literal null}.
     */
    private void deleteCascade(Resource resource) {
        if (resource.getOwners() != null) {
            resource.getOwners().forEach(o -> {
                        if (o != null)
                            ownerRepository.deleteById(o.getId());
                    }
            );
        }
    }

    /**
     * Saves a given resource. It also saves all owners of this resource.
     *
     * @param resource - must not be {@literal null}.
     */
    private void saveCascade(Resource resource) {
        resource.setOwners(resource.getOwners() == null ? null :
                resource.getOwners().stream().map(o -> {
                    if (o == null)
                        return null;
                    return ownerRepository.save(o);
                }).toList());
    }


    /**
     * Deletes a resource by id. It also deletes all owners of this resource.
     *
     * @param id - id of the resource to be deleted.
     * @return - true if deleted successfully.
     */
    @Override
    @CacheEvict(key = "#id")
    public boolean deleteById(String id) {
        Resource resource = resourceRepository.findById(id).orElseThrow(() ->
                new ResourceServiceException("Resource with id " + id + " not found"));
        deleteCascade(resource);
        resourceRepository.deleteById(id);
        return true;
    }

    /**
     * Deletes all resources. It also deletes all owners.
     *
     * @return - true if deleted successfully.
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean deleteAll() {
        resourceRepository.deleteAll();
        ownerRepository.deleteAll();
        return true;
    }

    /**
     * Updates a resource by id.
     *
     * @param id       - id of the resource to be updated.
     * @param resource - updated version of the resource.
     * @param cascade  - if true, it also updates all owners of this resource.
     * @return - updated version of the resource.
     */
    @Override
    @CachePut(key = "#id")
    public Resource updateById(String id, Resource resource, boolean cascade) {
        resource = resourceEncryptor.decrypt(resource);
        if (!resourceRepository.existsById(id))
            throw new ResourceServiceException("Resource with id " + id + " not found");
        resource.setId(id);
        if (cascade)
            saveCascade(resource);
        return resourceEncryptor.encrypt(resourceRepository.save(resource));
    }

    /**
     * Returns all resources in pages.
     *
     * @param pr - page request of type {@link PageRequest}.
     * @return - all resources in pages.
     */
    @Override
    public Page<Resource> getAllPaged(PageRequest pr) {
        return new PageImpl<>(
                resourceRepository.findAll(pr).stream().map(resourceEncryptor::encrypt).toList()
        );
    }
}
