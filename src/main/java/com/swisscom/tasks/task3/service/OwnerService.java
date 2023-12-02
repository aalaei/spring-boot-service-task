package com.swisscom.tasks.task3.service;

import com.swisscom.tasks.task3.model.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface OwnerService {
    Owner create(Owner owner, String resourceID);

    List<Owner> getAll();

    Optional<Owner> getById(String id);

    boolean deleteById(String id);

    boolean deleteAll();

    Owner updateById(String id, Owner owner);

    Page<Owner> getAllPaged(PageRequest pr);
}
