package com.swisscom.tasks.task3.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * This class is used to represent and store service object.
 */
@Data
@Builder
@Document(collection = "service")
@JsonInclude(NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ServiceO implements Serializable {
    @Id
    private String id;
    @Indexed
    private String criticalText;
    @DBRef
    private List<Resource> resources;
}
