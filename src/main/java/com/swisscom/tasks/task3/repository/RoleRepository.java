package com.swisscom.tasks.task3.repository;

import com.swisscom.tasks.task3.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByAuthority(String authority);
}
