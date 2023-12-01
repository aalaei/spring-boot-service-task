package com.swisscom.tasks.task3.dto.mapper;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.swisscom.tasks.task3.dto.ServiceIdDTO;
import com.swisscom.tasks.task3.dto.ServiceODTO;
import com.swisscom.tasks.task3.model.ServiceO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ServiceODTOMapper {
    public static ServiceODTO fromServiceO(ServiceO serviceO) {
        ServiceODTO serviceODTO=new ServiceODTO();
        BeanUtils.copyProperties(serviceO, serviceODTO);
        return serviceODTO;
    }
    public static ServiceO toServiceO(ServiceODTO serviceODTO) {
        ServiceO serviceO=new ServiceO();
        BeanUtils.copyProperties(serviceODTO, serviceO);
        return serviceO;
    }

    public static Collection<ServiceODTO> fromServiceOs(Collection<ServiceO> serviceOs) {
        return serviceOs.stream().map(ServiceODTOMapper::fromServiceO).collect(Collectors.toList());
    }
    public static Collection<ServiceO> toServiceOs(Collection<ServiceODTO> serviceODTOs) {
        return serviceODTOs.stream().map(ServiceODTOMapper::toServiceO).collect(Collectors.toList());
    }

    public static Optional<ServiceODTO> fromOptionalServiceO(Optional<ServiceO> serviceO) {
        if(serviceO.isEmpty())
            return Optional.empty();
        return serviceO.map(ServiceODTOMapper::fromServiceO);
    }
    public static Optional<ServiceO> toOptionalServiceO(Optional<ServiceODTO> serviceODTO) {
        if(serviceODTO.isEmpty())
            return Optional.empty();
        return serviceODTO.map(ServiceODTOMapper::toServiceO);
    }
}
