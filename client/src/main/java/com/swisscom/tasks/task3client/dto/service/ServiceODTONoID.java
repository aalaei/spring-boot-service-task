package com.swisscom.tasks.task3client.dto.service;

import com.swisscom.tasks.task3client.dto.owner.OwnerDTO;
import com.swisscom.tasks.task3client.dto.resource.ResourceDTO;
import lombok.EqualsAndHashCode;

/**
 * Service Object DTO With ResourceDTO and Owner DTO(without any id).
 */
@EqualsAndHashCode(callSuper = false)
public class ServiceODTONoID extends ServiceODTO<ResourceDTO<OwnerDTO>> {
}
