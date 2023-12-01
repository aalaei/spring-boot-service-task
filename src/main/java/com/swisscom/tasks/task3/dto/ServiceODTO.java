package com.swisscom.tasks.task3.dto;
import com.swisscom.tasks.task3.model.Resource;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ServiceODTO implements  Serializable{
    private String criticalText;
    private List<Resource> resources;
}
