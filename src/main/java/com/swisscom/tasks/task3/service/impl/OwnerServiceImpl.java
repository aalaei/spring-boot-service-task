package com.swisscom.tasks.task3.service.impl;

import com.swisscom.tasks.task3.crypto.service.OwnerEncryptor;
import com.swisscom.tasks.task3.exception.EncryptionException;
import com.swisscom.tasks.task3.exception.OwnerServiceException;
import com.swisscom.tasks.task3.model.Owner;
import com.swisscom.tasks.task3.model.Resource;
import com.swisscom.tasks.task3.repository.OwnerRepository;
import com.swisscom.tasks.task3.repository.ResourceRepository;
import com.swisscom.tasks.task3.service.OwnerService;
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
@CacheConfig(cacheNames = "owner")
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final ResourceRepository resourceRepository;
    private final OwnerEncryptor ownerEncryptor;

    /**
     * Saves a given owner. It also adds the owner to the parent resource.
     *
     * @param owner      - must not be {@literal null}.
     * @param resourceID - id of the parent resource.
     * @return - created owner.
     */
    @Override
    public Owner create(Owner owner, String resourceID) {
        try {
            owner = ownerEncryptor.decrypt(owner);
            Resource parentResource = resourceRepository
                    .findById(resourceID).orElseThrow(() ->
                            new OwnerServiceException("Resource with id " + resourceID + " not found"));
            Owner newOwner = ownerRepository.save(owner);
            if (parentResource.getOwners() == null) {
                parentResource.setOwners(List.of(newOwner));
            } else {
                parentResource.getOwners().add(newOwner);
            }
            resourceRepository.save(parentResource);
            return ownerEncryptor.encrypt(newOwner);
        }catch (EncryptionException e){
            throw new OwnerServiceException(e.getMessage());
        }
    }

    /**
     * Returns all owners.
     *
     * @return - all owners.
     */
    @Override
    public List<Owner> getAll() {
        return ownerRepository.findAll().stream().map(ownerEncryptor::encrypt).toList();
    }

    /**
     * Returns an owner by id.
     *
     * @param id - id of the owner.
     * @return - an owner by id.
     */
    @Override
    @Cacheable(key = "#id")
    public Optional<Owner> getById(String id) {
        return ownerRepository.findById(id).map(ownerEncryptor::encrypt);
    }

    /**
     * Deletes an owner by id.
     *
     * @param id - id of the owner.
     * @return - true if deleted, false if not found.
     */
    @Override
    @CacheEvict(key = "#id")
    public boolean deleteById(String id) {
        if (!ownerRepository.existsById(id))
            return false;
        ;
        ownerRepository.deleteById(id);
        return true;
    }

    /**
     * Deletes all owners.
     *
     * @return - true if deleted, false if not found.
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean deleteAll() {
        ownerRepository.deleteAll();
        return true;
    }

    /**
     * Updates an owner by id.
     *
     * @param id    - id of the owner.
     * @param owner - owner object.
     * @return - updated owner.
     */
    @Override
    @CachePut(key = "#id")
    public Owner updateById(String id, Owner owner) {
        try {
            owner = ownerEncryptor.decrypt(owner);
            if (!ownerRepository.existsById(id))
                return null;
            owner.setId(id);
            return ownerEncryptor.encrypt(ownerRepository.save(owner));
        }catch (EncryptionException e){
            throw new OwnerServiceException(e.getMessage());
        }

    }

    /**
     * Returns all owners in Pages.
     *
     * @param pr - {@link  PageRequest} object.
     * @return - all owners in Pages of {@link Owner}.
     */
    @Override
    public Page<Owner> getAllPaged(PageRequest pr) {
        return new PageImpl<>(
                ownerRepository.findAll(pr).stream().map(ownerEncryptor::encrypt).toList()
        );
    }
}
