package com.swisscom.tasks.task3.service.impl;

import com.swisscom.tasks.task3.crypto.service.ServiceOEncryptor;
import com.swisscom.tasks.task3.dto.service.ServiceIdDTO;
import com.swisscom.tasks.task3.exception.EncryptionException;
import com.swisscom.tasks.task3.exception.ServiceOServiceException;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.repository.OwnerRepository;
import com.swisscom.tasks.task3.repository.ResourceRepository;
import com.swisscom.tasks.task3.repository.ServiceORepository;
import com.swisscom.tasks.task3.service.ServiceOService;
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

/**
 * Implementation of {@link ServiceOService} interface.
 * Wrapper for {@link ServiceORepository},{@link ResourceRepository},{@link  OwnerRepository} + business logic.
 */
@RequiredArgsConstructor
@Service
@Slf4j
@CacheConfig(cacheNames = "service")
public class ServiceOServiceImpl implements ServiceOService {

    private final ServiceORepository serviceORepository;
    private final ResourceRepository resourceRepository;
    private final OwnerRepository ownerRepository;
    private final ServiceOEncryptor serviceOEncryptor;

    /**
     * Saves a given service. It also saves all resources and owners of this service.
     *
     * @param serviceO - must not be {@literal null}.
     */
    private void saveCascade(ServiceO serviceO) {
        if (serviceO.getResources() != null) {
            serviceO.getResources().forEach(r -> {
                if (r == null)
                    return;
                r.setOwners(r.getOwners() == null ? null :
                        r.getOwners().stream().map(o -> {
                            if (o == null)
                                return null;
                            return ownerRepository.save(o);
                        }).toList());
            });
            serviceO.setResources(serviceO.getResources().stream().map(resourceRepository::save).toList());
        }
    }

    /**
     * Deletes a given service. It also deletes all resources and owners of this service.
     *
     * @param serviceO - must not be {@literal null}.
     */
    private void deleteCascade(ServiceO serviceO) {
        if (serviceO.getResources() != null) {
            serviceO.getResources().forEach(r -> {
                if (r != null) {
                    if (r.getOwners() != null) {
                        r.getOwners().forEach(o -> {
                                    if (o != null)
                                        ownerRepository.deleteById(o.getId());
                                }
                        );
                    }
                    resourceRepository.deleteById(r.getId());
                }
            });
        }
    }

    /**
     * Saves a given service.
     *
     * @param serviceO must not be {@literal null}.
     * @return the saved service will never be {@literal null}.
     * @throws ServiceOServiceException if service with same id already exists.
     */
    @Override
    public ServiceO create(ServiceO serviceO) {
        try {
            serviceO = serviceOEncryptor.decrypt(serviceO);
            if (serviceO.getResources() == null)
                serviceO.setResources(List.of());
            if (serviceO.getId() != null && serviceORepository.existsById(serviceO.getId()))
                throw new ServiceOServiceException("Another service with id " + serviceO.getId() + " exists before");
            log.info("Saving a new service");
            saveCascade(serviceO);
            return serviceOEncryptor.encrypt(serviceORepository.save(serviceO));
        }catch (EncryptionException e){
            throw new ServiceOServiceException(e.getMessage());
        }
    }

    /**
     * Retrieves all services(Only ID).
     *
     * @return A List of all services(Only ID).
     */
    @Override
    public List<ServiceIdDTO> getAllIds() {
        log.info("Getting all services(Ids)");
        return serviceORepository.findAllIds();
    }

    /**
     * Retrieves all services.
     *
     * @return all services.
     */
    @Override
    public List<ServiceO> getAllDetailed() {
        log.info("Getting all services(Detailed)");
        return serviceORepository.findAll().stream().map(serviceOEncryptor::encrypt).toList();
    }

    /**
     * Retrieves a service by its id.
     *
     * @param id must not be {@literal null}.
     * @return the service with the given id or {@literal Optional#empty()} if none found.
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    @Override
    @Cacheable(key = "#id")
    public Optional<ServiceO> getById(String id) {
        log.info("Getting service with id {}", id);
        Optional<ServiceO> plainServiceO = serviceORepository.findById(id);
        return plainServiceO.map(serviceOEncryptor::encrypt);
    }

    /**
     * Deletes the service with the given id. It also deletes all resources and owners of this service.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if service was deleted, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}.
     */
    @Override
    @CacheEvict(key = "#id")
    public boolean deleteById(String id) {
        Optional<ServiceO> serviceO = serviceORepository.findById(id);
        log.info("Deleting service with id {}", id);
        if (serviceO.isPresent()) {
            if (serviceO.get().getResources() != null) {
                deleteCascade(serviceO.get());
            }
            serviceORepository.deleteById(id);
            return true;
        } else
            throw new ServiceOServiceException("Service with id " + id + " does not exists");
    }

    /**
     * Deletes all services. It also deletes all resources and owners.
     *
     * @return {@literal true} if all services were deleted, {@literal false} otherwise.
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean deleteAll() {
        log.info("Deleting all services");
        serviceORepository.deleteAll();
        ownerRepository.deleteAll();
        resourceRepository.deleteAll();
        return true;
    }

    /**
     * Updates the service with the given id. It also updates all resources and owners of this service.
     *
     * @param id       must not be {@literal null}.
     * @param serviceO must not be {@literal null}.
     * @return {@literal true} if service was updated, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}.
     */
    @Override
    @CachePut(key = "#id")
    public ServiceO updateById(String id, ServiceO serviceO) {
        return updateById(id, serviceO, true);
    }

    /**
     * Updates the service with the given id. It also updates all resources and owners of this service.
     *
     * @param id       must not be {@literal null}.
     * @param serviceO must not be {@literal null}.
     * @param cascade  if true, it also updates all resources and owners of this service.
     * @return {@literal true} if service was updated, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}.
     */
    @Override
    public ServiceO updateById(String id, ServiceO serviceO, boolean cascade) {
        try {
            serviceO = serviceOEncryptor.decrypt(serviceO);
            log.info("Updating service with id {}", id);
            if (serviceORepository.existsById(id)) {
                serviceO.setId(id);
                if (cascade)
                    saveCascade(serviceO);
                return serviceOEncryptor.encrypt(serviceORepository.save(serviceO));
            }
            throw new ServiceOServiceException("Service with id " + id + " does not exists");
        }catch (EncryptionException e){
            throw new ServiceOServiceException(e.getMessage());
        }
    }

    @Override
    public Page<ServiceO> getAllPaged(PageRequest pr) {
        log.info("Getting all services(Detailed) in Pages");
        return new PageImpl<>(serviceORepository.findAll(pr).stream().map(serviceOEncryptor::encrypt)
                .toList());
    }
}
