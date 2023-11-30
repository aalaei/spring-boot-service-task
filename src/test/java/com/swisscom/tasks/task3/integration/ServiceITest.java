package com.swisscom.tasks.task3.integration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.tasks.task3.model.HttpResponse;
import com.swisscom.tasks.task3.model.Owner;
import com.swisscom.tasks.task3.model.Resource;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.repository.ServiceORepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ServiceITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceORepository serviceRepository;

    @BeforeEach
    void setUp() {
        serviceRepository.deleteAll();
    }
    @Test
    void canAddNewService() throws Exception {
        // given
        ServiceO serviceO = new ServiceO(
                null,
                "criticalText",
                List.of(
                        Resource.builder()
                                .criticalText("criticalText1")
                                .owners(List.of(
                                        Owner.builder()
                                                .id("id")
                                                .criticalText("criticalText2")
                                                .name("name")
                                                .accountNumber("accountNumber")
                                                .level(1)
                                                .build()
                                ))
                                .build()
                )
        );

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceO)));
        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.name()))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.service").exists())
                .andExpect(jsonPath("$.data.service.criticalText").value(serviceO.getCriticalText()))
                .andExpect(jsonPath("$.data.service.resources[0].criticalText")
                        .value(serviceO.getResources().get(0).getCriticalText()))
                .andExpect(jsonPath("$.data.service.resources[0].owners[0].criticalText")
                        .value(serviceO.getResources().get(0).getOwners().get(0).getCriticalText()));

        List<ServiceO> services = serviceRepository.findAll();

//        assertThat(services)
//                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
//                .contains(serviceO);
    }

    @Test
    void canDeleteService() throws Exception {
        // given
        ServiceO serviceO = new ServiceO(
                "id",
                "criticalText0",
                List.of(
                        Resource.builder()
                                .criticalText("criticalText1")
                                .owners(List.of(
                                        Owner.builder()
                                                .id("id")
                                                .criticalText("criticalText2")
                                                .name("name")
                                                .accountNumber("accountNumber")
                                                .level(1)
                                                .build()
                                ))
                                .build()
                )
        );

        mockMvc.perform(post("/api/v1/service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceO)))
                .andExpect(status().isCreated());

        MvcResult getServicesResult = mockMvc.perform(get("/api/v1/service")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getServicesResult
                .getResponse()
                .getContentAsString();

        HttpResponse httpResponse = objectMapper.readValue(
                contentAsString,
                HttpResponse.class
        );
        List<ServiceO> services = objectMapper.convertValue(
                httpResponse.getData().get("services"),
                new TypeReference<>() {
                }
        );

        String id = services.stream()
                .filter(s -> s.getCriticalText().equals(serviceO.getCriticalText()) && (
                  s.getResources().get(0).getOwners().get(0).getCriticalText()
                          .equals(serviceO.getResources().get(0).getOwners().get(0).getCriticalText())
                ))
                .map(ServiceO::getId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Service with the specified criteria not found"));
        // when
        ResultActions resultActions = mockMvc
                .perform(delete("/api/v1/service/" + id));

        // then
        resultActions.andExpect(status().isOk());
        boolean exists = serviceRepository.existsById(id);
        assertThat(exists).isFalse();
    }
    @Test
    void ThrowWhenDeleteServiceNotFound() throws Exception {
        // given
        String id = "id";
        // when
        ResultActions resultActions = mockMvc
                .perform(delete("/api/v1/service/" + id));
        // then
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Not Found"));
    }
    @Test
    void canEditService()throws Exception {
        // given
        ServiceO serviceO = new ServiceO(
                "id",
                "criticalText0",
                List.of(
                        Resource.builder()
                                .criticalText("criticalText1")
                                .owners(List.of(
                                        Owner.builder()
                                                .id("id")
                                                .criticalText("criticalText2")
                                                .name("name")
                                                .accountNumber("accountNumber")
                                                .level(1)
                                                .build()
                                ))
                                .build()
                )
        );
        ServiceO newService = new ServiceO(
                null,
                "newCriticalText0",
                List.of(
                        Resource.builder()
                                .criticalText("newCriticalText1")
                                .owners(List.of(
                                        Owner.builder()
                                                .criticalText("newCriticalText2")
                                                .name("newName")
                                                .accountNumber("newAccountNumber")
                                                .level(1)
                                                .build()
                                ))
                                .build()
                )
        );

        mockMvc.perform(post("/api/v1/service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceO)))
                .andExpect(status().isCreated());

        MvcResult getServicesResult = mockMvc.perform(get("/api/v1/service")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getServicesResult
                .getResponse()
                .getContentAsString();

        HttpResponse httpResponse = objectMapper.readValue(
                contentAsString,
                HttpResponse.class
        );
        List<ServiceO> services = objectMapper.convertValue(
                httpResponse.getData().get("services"),
                new TypeReference<>() {
                }
        );

        String id = services.stream()
                .filter(s -> s.getCriticalText().equals(serviceO.getCriticalText()) && (
                        s.getResources().get(0).getOwners().get(0).getCriticalText()
                                .equals(serviceO.getResources().get(0).getOwners().get(0).getCriticalText())
                ))
                .map(ServiceO::getId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Service with the specified criteria not found"));
        // when
        ResultActions resultActions = mockMvc
                .perform(put("/api/v1/service/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newService))
                );

        // then
        resultActions.andExpect(status().isOk());
        Optional<ServiceO> extractedService = serviceRepository.findById(id);
        //TODO add DTO equal
//        assertThat(extractedService).isEqualTo(Optional.of(newService));

    }
    @Test
    void ThrowWhenEditServiceNotFound() throws Exception {
        // given
        String id = "id";
        ServiceO newService = ServiceO.builder().build();
        // when
        ResultActions resultActions = mockMvc
                .perform(put("/api/v1/service/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newService))
                );
        // then
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Not Found"));
    }
}