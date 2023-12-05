package com.swisscom.tasks.task3client.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * This class is used to represent and store service object.
 */
@Data
public class ServiceO implements Serializable {
    private String id;
    private String criticalText;
    private List<Resource> resources;
}
