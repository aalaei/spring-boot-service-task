package com.swisscom.tasks.task3.dto.service;

import com.swisscom.tasks.task3.model.ServiceO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ServiceO DTO mapper. A Util class for mapping ServiceO DTOs to entities and vice versa(Legacy).
 */
@Component
public class ServiceODTOMapper {
    public static ServiceODTODefault fromServiceO(ServiceO serviceO) {
        ServiceODTODefault serviceODTO = new ServiceODTODefault();
        BeanUtils.copyProperties(serviceO, serviceODTO);
        return serviceODTO;
    }

    public static ServiceO toServiceO(ServiceODTODefault serviceODTO) {
        ServiceO serviceO = new ServiceO();
        BeanUtils.copyProperties(serviceODTO, serviceO);
        return serviceO;
    }

    public static Collection<ServiceODTODefault> fromServiceOs(Collection<ServiceO> serviceOs) {
        return serviceOs.stream().map(ServiceODTOMapper::fromServiceO).collect(Collectors.toList());
    }

    public static Collection<ServiceO> toServiceOs(Collection<ServiceODTODefault> serviceODTOs) {
        return serviceODTOs.stream().map(ServiceODTOMapper::toServiceO).collect(Collectors.toList());
    }

    public static Optional<ServiceODTODefault> fromOptionalServiceO(Optional<ServiceO> serviceO) {
        if (serviceO.isEmpty())
            return Optional.empty();
        return serviceO.map(ServiceODTOMapper::fromServiceO);
    }

    public static Optional<ServiceO> toOptionalServiceO(Optional<ServiceODTODefault> serviceODTO) {
        if (serviceODTO.isEmpty())
            return Optional.empty();
        return serviceODTO.map(ServiceODTOMapper::toServiceO);
    }
}
