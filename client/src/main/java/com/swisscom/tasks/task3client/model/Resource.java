package com.swisscom.tasks.task3client.model;

import lombok.Data;
import java.io.Serializable;
import java.util.List;


import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * This class is used to represent and store resource object.
 */
@Data
public class Resource implements Serializable {
    private String id;
    private String criticalText;
    private List<Owner> owners;
}
