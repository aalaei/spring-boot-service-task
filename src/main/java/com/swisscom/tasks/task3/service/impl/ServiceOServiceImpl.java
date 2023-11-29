package com.swisscom.tasks.task3.service.impl;

import com.swisscom.tasks.task3.model.ServiceO;
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
    @Override
    public String save(ServiceO serviceO) {
        return serviceORepository.save(serviceO).getId();
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
        serviceORepository.deleteById(id);
        return true;
    }

    @Override
    public boolean updateById(String id, ServiceO serviceO) {
        if (serviceORepository.existsById(id)) {
            serviceO.setId(id);
            serviceORepository.save(serviceO);
            return true;
        }
        return false;
    }
}
