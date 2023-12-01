package com.swisscom.tasks.task3.dto;

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
