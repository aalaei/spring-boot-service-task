package com.swisscom.tasks.task3client.model;

import lombok.Data;

import java.io.Serializable;

/**
 * This class is used to represent and store owner object.
 */
@Data
public class Owner implements Serializable {
    private String id;
    private String criticalText;
    private String name;
    private String accountNumber;
    private Integer level;
}
