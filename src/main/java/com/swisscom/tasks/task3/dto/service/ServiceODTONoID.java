package com.swisscom.tasks.task3.dto.service;

import com.swisscom.tasks.task3.dto.owner.OwnerDTO;
import com.swisscom.tasks.task3.dto.resource.ResourceDTO;
import lombok.EqualsAndHashCode;

/**
 * Service Object DTO With ResourceDTO and Owner DTO(without any id).
 */
@EqualsAndHashCode(callSuper = false)
public class ServiceODTONoID extends ServiceODTO<ResourceDTO<OwnerDTO>> {
}
