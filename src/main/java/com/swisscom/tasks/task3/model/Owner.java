package com.swisscom.tasks.task3.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class Owner {
    private String id;
    private String criticalText;
    private String name;
    private String accountNumber;
    private Integer level;
}
