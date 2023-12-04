package com.swisscom.tasks.task3client.dto.resource;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Resource DTO.
 */
@Data
public class ResourceDTO<T> implements Serializable {
    private String criticalText;
    private List<T> owners;
}
