package com.swisscom.tasks.task3.controller.graphql;

import com.swisscom.tasks.task3.dto.mapper.DTOMapper;
import com.swisscom.tasks.task3.model.Owner;
import com.swisscom.tasks.task3.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

/**
 * GraphQL controller for Owner.
 */
@Controller
@RequiredArgsConstructor
public class OwnerGraphQLController {
    private final OwnerService ownerService;
    private final DTOMapper dtoMapper;

    /**
     * Returns an owner by id.
     *
     * @return all owners in Pages.
     */
    @QueryMapping
    Optional<Owner> owner(@Argument String id) {
        return ownerService.getById(id);
    }

    /**
     * Returns all owners.
     *
     * @return all owners.
     */
    @QueryMapping
    List<Owner> owners() {
        return ownerService.getAll();
    }

    /**
     * Created and Returns an owner
     *
     * @param owner      - owner Input object.
     * @param resourceId - id of the parent resource.
     * @return Created owner.
     */
    @MutationMapping
    Owner createOwner(@Argument OwnerInput owner, @Argument String resourceId) {
        Owner ownerO = dtoMapper.map(owner, Owner.class);
        ownerService.create(ownerO, resourceId);
        return ownerO;
    }

    /**
     * Updates an owner
     *
     * @param id    - id of the owner.
     * @param owner - owner Input object.
     * @return updated version of the owner.
     */
    @MutationMapping
    Owner updateOwner(@Argument String id, @Argument OwnerInput owner) {
        Owner ownerO = dtoMapper.map(owner, Owner.class);
        ownerService.updateById(id, ownerO);
        return ownerO;
    }

    /**
     * Deletes an owner by id.
     *
     * @param id - id of the owner.
     * @return deleted owner.
     */
    @MutationMapping
    Optional<Owner> deleteOwner(@Argument String id) {
        Optional<Owner> oldOwner = ownerService.getById(id);
        ownerService.deleteById(id);
        return oldOwner;
    }

    record OwnerInput(String criticalText, String name, String accountNumber, Integer level) {
    }

}
