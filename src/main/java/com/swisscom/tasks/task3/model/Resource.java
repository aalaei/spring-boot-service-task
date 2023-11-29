package com.swisscom.tasks.task3.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
@Builder
public class Resource {
    private String id;
    private String criticalText;
    private List<Owner> owners;
}
