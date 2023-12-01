package com.swisscom.tasks.task3.dto.service;

import com.swisscom.tasks.task3.dto.OwnerDTO;
import com.swisscom.tasks.task3.dto.ResourceDTO;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class ServiceODTONoID extends ServiceODTO<ResourceDTO<OwnerDTO>> {
}
