package com.swisscom.tasks.task3client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.tasks.task3client.dto.service.ServiceIdDTO;
import com.swisscom.tasks.task3client.dto.service.ServiceODTODefault;
import com.swisscom.tasks.task3client.exception.HttpCallException;
import com.swisscom.tasks.task3client.model.ServiceO;
import com.swisscom.tasks.task3client.model.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParsedJsonService {
    private final JsonPlaceholderService client;

    public <T> T parseResponse(ResponseEntity<?> response, String path, String errorMessage, Class<T> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(parseResponse(response, path, errorMessage), type);
        } catch (Exception e) {
            throw new HttpCallException(errorMessage, ResponseEntity.status(500).build());
        }
    }

    public <T> T parseResponse(ResponseEntity<?> response, String path, String errorMessage) {
        try {
            if (response.getStatusCode().isError())
                throw new HttpCallException(errorMessage, response);
            ObjectMapper mapper = new ObjectMapper();

            HttpResponse httpResponse =
                    mapper.convertValue(response.getBody(), new TypeReference<>() {
                    });
            return mapper.convertValue(httpResponse.getData().get(path), new TypeReference<T>() {
            });
        } catch (Exception e) {
            throw new HttpCallException(errorMessage, ResponseEntity.status(500).build());
        }
    }

    public List<String> findAllIds() {
        try {
            ResponseEntity<?> response = client.findAllIds();
            List<?> rawServices = parseResponse(response, "services",
                    "Error while fetching all ids");
            ObjectMapper mapper = new ObjectMapper();
            return rawServices.stream().map(o -> mapper.convertValue(o, ServiceIdDTO.class))
                    .map(ServiceIdDTO::getId).toList();
        } catch (RestClientResponseException exception) {
            ResponseEntity<?> response = ResponseEntity.status(exception.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(exception.getResponseBodyAsString());
            throw new HttpCallException("Error while fetching all ids", response);

        }
    }

    //    @Override
    public Optional<ServiceODTODefault> getByID(String id) {
        try {
            ResponseEntity<?> response = client.getByID(id);
            var a = parseResponse(response, "service", "Error while getting the service",
                    ServiceODTODefault.class
            );
            return a == null ? Optional.empty() : Optional.of(a);
        } catch (RestClientResponseException exception) {
            ResponseEntity<?> response = ResponseEntity.status(exception.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(exception.getResponseBodyAsString());
            throw new HttpCallException("Error while getting the service", response);

        }
    }

    //    @Override
    public ServiceO createService(ServiceODTODefault serviceODTO) {
        try {
            ResponseEntity<?> response = client.createService(serviceODTO);
            return parseResponse(response, "service", "Error while creating the service",
                    ServiceO.class
            );
        } catch (RestClientResponseException exception) {
            ResponseEntity<?> response = ResponseEntity.status(exception.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(exception.getResponseBodyAsString());
            throw new HttpCallException("Error while creating the service", response);

        }
    }

    //    @Override
    public ServiceODTODefault updateService(String id, ServiceODTODefault serviceODTO) {
        try {
            ResponseEntity<?> response = client.updateService(id, serviceODTO);
            return parseResponse(response, "service", "Error while updating the service",
                    ServiceODTODefault.class
            );
        } catch (RestClientResponseException exception) {
            ResponseEntity<?> response = ResponseEntity.status(exception.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(exception.getResponseBodyAsString());
            throw new HttpCallException("Error while updating the service", response);

        }
    }

    //    @Override
    public void deleteService(String id) {
        try {
            ResponseEntity<?> response = client.deleteService(id);
//                parseResponse(response, "service", "Error while deleting the service");
        } catch (RestClientResponseException exception) {
            ResponseEntity<?> response = ResponseEntity.status(exception.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(exception.getResponseBodyAsString());
            throw new HttpCallException("Error while deleting the service", response);

        }
    }

}
