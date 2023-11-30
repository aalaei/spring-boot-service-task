package com.swisscom.tasks.task3.service.impl;

import com.swisscom.tasks.task3.exception.ServiceOServiceException;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.repository.OwnerRepository;
import com.swisscom.tasks.task3.repository.ResourceRepository;
import com.swisscom.tasks.task3.repository.ServiceORepository;
import com.swisscom.tasks.task3.service.ServiceOService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ServiceOServiceImpl implements ServiceOService {

    private final ServiceORepository serviceORepository;
    private final ResourceRepository resourceRepository;
    private final OwnerRepository ownerRepository;

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

    @Override
    public List<ServiceO> getAll() {
        return serviceORepository.findAll();
    }

    @Override
    public Optional<ServiceO> getById(String id) {
        return serviceORepository.findById(id);
    }

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
