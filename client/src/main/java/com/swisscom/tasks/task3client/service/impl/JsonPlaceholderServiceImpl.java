package com.swisscom.tasks.task3client.service.impl;

import com.swisscom.tasks.task3client.dto.auth.LoginRequestDTO;
import com.swisscom.tasks.task3client.dto.auth.LoginResponseDTO;
import com.swisscom.tasks.task3client.dto.service.ServiceIdDTO;
import com.swisscom.tasks.task3client.dto.service.ServiceODTODefault;
import com.swisscom.tasks.task3client.exception.HttpCallException;
import com.swisscom.tasks.task3client.model.ServiceO;
import com.swisscom.tasks.task3client.model.http.HttpResponseServiceDTODefault;
import com.swisscom.tasks.task3client.model.http.HttpResponseServiceO;
import com.swisscom.tasks.task3client.model.http.httpResponseIds;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

//@Service
@Slf4j
public class JsonPlaceholderServiceImpl {//implements JsonPlaceholderService {
    private final RestClient restClient;

    public JsonPlaceholderServiceImpl(Environment environment) {
        String username = environment.getProperty("application.connection.security.auth.user", "");
        String password = environment.getProperty("application.connection.security.auth.pass", "");
        String url = environment.getProperty("application.connection.url", "http://localhost:8080");
        RestClient.Builder restBuilder = RestClient.builder()
                .baseUrl(url + "/api/v1");
        if (!username.isEmpty()) {
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(username, password);
            String jwtToken = Objects.requireNonNull(RestClient.builder()
                    .baseUrl(url + "/api/v1/auth/login")
                    .build()
                    .post().body(loginRequestDTO)
                    .retrieve()
                    .body(LoginResponseDTO.class)).getJwt();
            restBuilder = restBuilder
                    .defaultHeader("Authorization", "Bearer " + jwtToken);
            log.info("Logged in successfully as " + username);
        }
        restClient = restBuilder.build();
    }

    //    @Override
    public List<String> findAllIds() {
        try {
            ResponseEntity<httpResponseIds> httpResponse = restClient.get()
                    .uri("/services")
                    .retrieve()
                    .toEntity(httpResponseIds.class);
            if (httpResponse.getStatusCode().isError() || httpResponse.getBody() == null)
                throw new HttpCallException("Error while fetching all ids", httpResponse);
            return httpResponse.getBody().getData().get("services").stream().map(ServiceIdDTO::getId).toList();
        } catch (Exception e) {
            throw new HttpCallException("Error while fetching all ids", ResponseEntity.status(500).build());
        }
    }

    //    @Override
    public Optional<ServiceODTODefault> getByID(String id) {
        ResponseEntity<HttpResponseServiceDTODefault> httpResponse = restClient.get()
                .uri("/services?id=" + id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        (req, res) ->
                        {
                            log.error("Couldn't delete " + res.getStatusText());
                            String bodyStr = new String(res.getBody().readAllBytes());

                            throw new HttpCallException("Error while getting the service",
                                    ResponseEntity.status(res.getStatusCode()).body(res.getBody().readAllBytes())
                            );
                        }
                ).toEntity(HttpResponseServiceDTODefault.class);
        try {
            if (httpResponse.getStatusCode().isError() || httpResponse.getBody() == null)
                throw new Exception("");
            return Optional.of(httpResponse.getBody().getData().get("service"));
        } catch (Exception e) {
            throw new HttpCallException("Error while getting the service", httpResponse);
        }
    }

    //    @Override
    public ServiceO createService(ServiceODTODefault serviceODTO) {
        ResponseEntity<HttpResponseServiceO> httpResponse = restClient.post()
                .uri("/services")
                .contentType(MediaType.APPLICATION_JSON)
                .body(serviceODTO)
                .retrieve()
                .toEntity(HttpResponseServiceO.class);
        try {
            if (httpResponse.getStatusCode().isError() || httpResponse.getBody() == null)
                throw new Exception("");
            return httpResponse.getBody().getData().get("service");
        } catch (Exception e) {
            throw new HttpCallException("Error while creating the service", httpResponse);
        }
    }

    //    @Override
    public ServiceODTODefault updateService(String id, ServiceODTODefault serviceODTO) {
        ResponseEntity<HttpResponseServiceDTODefault> httpResponse = restClient.put()
                .uri("/services?id=" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(serviceODTO)
                .retrieve()
                .toEntity(HttpResponseServiceDTODefault.class);
        try {
            if (httpResponse.getStatusCode().isError() || httpResponse.getBody() == null)
                throw new Exception("");
            return httpResponse.getBody().getData().get("service");
        } catch (Exception e) {
            throw new HttpCallException("Error while updating the service", httpResponse);
        }
    }

    //    @Override
    public void deleteService(String id) {
        ResponseEntity<Void> httpResponse = restClient.delete()
                .uri("/services?id=" + id)
                .retrieve()
                .toBodilessEntity();
        if (httpResponse.getStatusCode().isError())
            throw new HttpCallException("Error while deleting the service", httpResponse);
    }
}
