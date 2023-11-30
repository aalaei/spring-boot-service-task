package com.swisscom.tasks.task3.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@Document(collection = "service")
@JsonInclude(NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ServiceO {
    @Id
    private String id;
    private String criticalText;
    private List<Resource> resources;
}
