package com.swisscom.tasks.task3client.service.impl;

import com.swisscom.tasks.task3client.dto.service.ServiceIdDTO;
import com.swisscom.tasks.task3client.dto.service.ServiceODTODefault;
import com.swisscom.tasks.task3client.exception.HttpCallException;
import com.swisscom.tasks.task3client.model.http.*;
import com.swisscom.tasks.task3client.model.ServiceO;
import com.swisscom.tasks.task3client.service.JsonPlaceholderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class JsonPlaceholderServiceImpl implements JsonPlaceholderService {
    private final RestClient restClient;
    public JsonPlaceholderServiceImpl() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:8080/api/v1")
                .build();
    }
    @Override
    public List<String> findAllIds() {
        ResponseEntity<httpResponseIds> httpResponse= restClient.get()
                .uri("/services")
                .retrieve()
                .toEntity(httpResponseIds.class);
        if(httpResponse.getStatusCode().isError() || httpResponse.getBody()==null)
            throw new HttpCallException("Error while fetching all ids", httpResponse);
        return httpResponse.getBody().getData().get("services").stream().map(ServiceIdDTO::getId).toList();
    }

    @Override
    public Optional<ServiceODTODefault> getByID(String id) {
        ResponseEntity<HttpResponseServiceDTODefault> httpResponse = restClient.get()
                .uri("/services?id=" + id)
                .retrieve()
                .toEntity(HttpResponseServiceDTODefault.class);
        try {
            if(httpResponse.getStatusCode().isError() || httpResponse.getBody()==null)
                throw new Exception("");
            return Optional.of(httpResponse.getBody().getData().get("service"));
        } catch (Exception e){
            throw new HttpCallException("Error while getting the service", httpResponse);
        }
    }

    @Override
    public ServiceO createService(ServiceODTODefault serviceODTO) {
        ResponseEntity<HttpResponseServiceO> httpResponse= restClient.post()
                .uri("/services")
                .contentType(MediaType.APPLICATION_JSON)
                .body(serviceODTO)
                .retrieve()
                .toEntity(HttpResponseServiceO.class);
        try {
            if(httpResponse.getStatusCode().isError() || httpResponse.getBody()==null)
                throw new Exception("");
            return httpResponse.getBody().getData().get("service");
        } catch (Exception e){
            throw new HttpCallException("Error while creating the service", httpResponse);
        }
    }

    @Override
    public ServiceODTODefault updateService(String id, ServiceODTODefault serviceODTO) {
        ResponseEntity<HttpResponseServiceDTODefault> httpResponse= restClient.put()
                .uri("/services?id="+id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(serviceODTO)
                .retrieve()
                .toEntity(HttpResponseServiceDTODefault.class);
        try {
            if(httpResponse.getStatusCode().isError() || httpResponse.getBody()==null)
                throw new Exception("");
            return httpResponse.getBody().getData().get("service");
        } catch (Exception e){
            throw new HttpCallException("Error while updating the service", httpResponse);
        }
    }

    @Override
    public void deleteService(String id) {
        ResponseEntity<Void> httpResponse= restClient.delete()
                .uri("/services?id="+id)
                .retrieve()
                .toBodilessEntity();
        if(httpResponse.getStatusCode().isError())
            throw new HttpCallException("Error while deleting the service", httpResponse);
    }
}
