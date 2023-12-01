package com.swisscom.tasks.task3.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OwnerDTO implements Serializable {
    private String criticalText;
    private String name;
    private String accountNumber;
    private Integer level;
}
