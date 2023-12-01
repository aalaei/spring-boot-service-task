package com.swisscom.tasks.task3.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class DTOMapper {
    private final ModelMapper modelMapper;

    public <T> T map(Object o, Class<T> c) {
        return modelMapper.map(o, c);
    }

    public <T, V> Optional<T> mapOptional(Optional<V> o, Class<T> c) {
        if (o.isEmpty())
            return Optional.empty();
        return Optional.of(modelMapper.map(o, c));
    }

    public void map(Object o, Object c) {
        modelMapper.map(o, c);
    }
}
