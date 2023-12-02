package com.swisscom.tasks.task3.service.impl;

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
    @Override
    public Owner create(Owner owner, String resourceID)
    {
        Resource parentResource= resourceRepository
                .findById(resourceID).orElseThrow(()->
                        new OwnerServiceException("Resource with id "+resourceID+" not found"));
        Owner newOwner= ownerRepository.save(owner);
        if(parentResource.getOwners()==null){
            parentResource.setOwners(List.of(newOwner));
        }else{
            parentResource.getOwners().add(newOwner);
        }
        resourceRepository.save(parentResource);
        return newOwner;
    }

    @Override
    public List<Owner> getAll() {
        return ownerRepository.findAll();
    }

    @Override
    @Cacheable(key = "#id")
    public Optional<Owner> getById(String id) {
        return ownerRepository.findById(id);
    }

    @Override
    @CacheEvict(key = "#id")
    public boolean deleteById(String id) {
        if(!ownerRepository.existsById(id))
            return false;;
        ownerRepository.deleteById(id);
        return true;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean deleteAll() {
        ownerRepository.deleteAll();
        return true;
    }

    @Override
    @CachePut(key = "#id")
    public Owner updateById(String id, Owner owner) {
        if(!ownerRepository.existsById(id))
            return null;
        owner.setId(id);
        return ownerRepository.save(owner);
    }

    @Override
    public Page<Owner> getAllPaged(PageRequest pr) {
        return ownerRepository.findAll(pr);
    }
}
