package com.swisscom.tasks.task3.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Owner {
    private String id;
    private String criticalText;
    private String name;
    private String accountNumber;
    private Integer level;
}
