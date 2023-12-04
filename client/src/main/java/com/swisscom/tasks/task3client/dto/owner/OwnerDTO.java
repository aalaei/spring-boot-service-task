package com.swisscom.tasks.task3client.dto.owner;

import lombok.Data;

import java.io.Serializable;

/**
 * Owner DTO.
 */
@Data
public class OwnerDTO implements Serializable {
    private String criticalText;
    private String name;
    private String accountNumber;
    private Integer level;
}
