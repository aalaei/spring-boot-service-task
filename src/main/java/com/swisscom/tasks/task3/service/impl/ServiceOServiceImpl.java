package com.swisscom.tasks.task3.service.impl;

import com.swisscom.tasks.task3.exception.ServiceOServiceException;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.repository.OwnerRepository;
import com.swisscom.tasks.task3.repository.ResourceRepository;
import com.swisscom.tasks.task3.repository.ServiceORepository;
import com.swisscom.tasks.task3.service.ServiceOService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ServiceOService} interface.
 * Wrapper for {@link ServiceORepository},{@link ResourceRepository},{@link  OwnerRepository} + business logic.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ServiceOServiceImpl implements ServiceOService {

    private final ServiceORepository serviceORepository;
    private final ResourceRepository resourceRepository;
    private final OwnerRepository ownerRepository;
    /**
     * Saves a given service.
     *
     * @param serviceO must not be {@literal null}.
     * @return the saved service will never be {@literal null}.
     * @throws ServiceOServiceException if service with same id already exists.
    */
    @Override
    public ServiceO save(ServiceO serviceO) {
        if (serviceO.getId() != null && serviceORepository.existsById(serviceO.getId()))
            throw new ServiceOServiceException("Another service with id "+serviceO.getId()+" exists before");
        serviceO.getResources().forEach(r -> {
            if(r!=null)
                r.setOwners(r.getOwners().stream().map(ownerRepository::save).toList());
        });
        serviceO.setResources(serviceO.getResources().stream().map(resourceRepository::save).toList());
        return serviceORepository.save(serviceO);
    }

    /**
     * Retrieves all services.
     *
     * @return all services.
    */
    @Override
    public List<ServiceO> getAll() {
        log.info("Getting all services");
        return serviceORepository.findAll();
    }

    /**
     * Retrieves a service by its id.
     *
     * @param id must not be {@literal null}.
     * @return the service with the given id or {@literal Optional#empty()} if none found.
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
    */
    @Override
    public Optional<ServiceO> getById(String id) {
        log.info("Getting service with id {}", id);
        return serviceORepository.findById(id);
    }

    /**
     * Deletes the service with the given id. It also deletes all resources and owners of this service.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if service was deleted, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}.
    */
    @Override
    public boolean deleteById(String id) {
        Optional<ServiceO> serviceO = serviceORepository.findById(id);
        if (serviceO.isPresent()) {
            serviceO.get().getResources().forEach(r -> {
                if(r != null){
                    r.getOwners().forEach(o -> {
                        if (o != null)
                            ownerRepository.deleteById(o.getId());
                    }
                    );
                    resourceRepository.deleteById(r.getId());
                }
            });
            serviceORepository.deleteById(id);
            return true;
        } else
            throw new ServiceOServiceException("Service with id " + id + " does not exists");
//        return false;
    }
    /**
     * Updates the service with the given id. It also updates all resources and owners of this service.
     *
     * @param id must not be {@literal null}.
     * @param serviceO must not be {@literal null}.
     * @return {@literal true} if service was updated, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}.
    */
    @Override
    public boolean updateById(String id, ServiceO serviceO) {
        if (serviceORepository.existsById(id)) {
            serviceO.setId(id);
            serviceO.getResources().forEach(r ->
            {
                if(r!=null)
                    r.setOwners(r.getOwners().stream().map(ownerRepository::save).toList());
            });
            serviceO.setResources(serviceO.getResources().stream().map(resourceRepository::save).toList());
            serviceORepository.save(serviceO);
            return true;
        }
        throw new ServiceOServiceException("Service with id " + id + " does not exists");
    }
}
