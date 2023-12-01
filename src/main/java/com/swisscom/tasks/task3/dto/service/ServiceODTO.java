package com.swisscom.tasks.task3.dto.service;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data

public class ServiceODTO<T> implements Serializable {
    private String criticalText;
    private List<T> resources;
}
