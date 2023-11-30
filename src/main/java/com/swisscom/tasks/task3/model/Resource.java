package com.swisscom.tasks.task3.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
    private String id;
    private String criticalText;
    private List<Owner> owners;
}
