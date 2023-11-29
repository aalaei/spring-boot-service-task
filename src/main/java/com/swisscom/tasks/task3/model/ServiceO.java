package com.swisscom.tasks.task3.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "service")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceO {
    @Id
    private String id;
    private String criticalText;
    private List<Resource> resources;

}
