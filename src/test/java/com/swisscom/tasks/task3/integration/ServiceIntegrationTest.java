package com.swisscom.tasks.task3.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.tasks.task3.dto.mapper.DTOMapper;
import com.swisscom.tasks.task3.dto.service.ServiceODTODefault;
import com.swisscom.tasks.task3.dto.service.ServiceODTONoID;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
public class ServiceIntegrationTest {
    private final String serviceEndpoint = "/api/v1/services";

    @Autowired
    DTOMapper dTOMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceORepository serviceRepository;
    RequestPostProcessor authorities = SecurityMockMvcRequestPostProcessors.jwt()
            .authorities(new SimpleGrantedAuthority("SCOPE_SUPER_USER"));

    @BeforeEach
    void setUp() {
        serviceRepository.deleteAll();
    }

    @Test
    void isHealthy() throws Exception {
        // when
        String healthEndpoint = "/health";
        ResultActions resultActions = mockMvc
                .perform(get(healthEndpoint)
                        .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string("Healthy"));
    }

    @Test
    void hasSavedService() throws Exception {
        // given
        ServiceO serviceO = ServiceO.builder()
                .criticalText("criticalText")
                .resources(
                        List.of(
                                Resource.builder()
                                        .criticalText("criticalText1")
                                        .owners(
                                                List.of(
                                                        Owner.builder()
                                                                .id("id")
                                                                .criticalText("criticalText2")
                                                                .name("name")
                                                                .accountNumber("accountNumber")
                                                                .level(1)
                                                                .build()
                                                ))
                                        .build()
                        ))
                .build();
        MvcResult getServicesResult = mockMvc.perform(post(serviceEndpoint)
                        .with(authorities)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceO)))
                .andExpect(status().isCreated())
                .andReturn();
        HttpResponse httpResponse = objectMapper.readValue(
                getServicesResult.getResponse().getContentAsString(),
                HttpResponse.class
        );
        ServiceO services = objectMapper.convertValue(
                httpResponse.getData().get("service"),
                new TypeReference<>() {
                }
        );
        ResultActions resultActions = mockMvc
                .perform(get(serviceEndpoint)
                        .with(authorities)
                        .param("id", services.getId())
                        .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.service").exists())
                .andExpect(jsonPath("$.data.service.criticalText").value(serviceO.getCriticalText()))
                .andExpect(jsonPath("$.data.service.resources[0].criticalText")
                        .value(serviceO.getResources().get(0).getCriticalText()))
                .andExpect(jsonPath("$.data.service.resources[0].owners[0].criticalText")
                        .value(serviceO.getResources().get(0).getOwners().get(0).getCriticalText()));
    }

    @Test
    void canAddNewService() throws Exception {
        // given
        ServiceODTODefault serviceODTO = dTOMapper.map(
                new ServiceO(
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
                )
                , ServiceODTODefault.class);

        // when
        ResultActions resultActions = mockMvc
                .perform(post(serviceEndpoint)
                        .with(authorities)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceODTO)));
        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.name()))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.service").exists())
                .andExpect(jsonPath("$.data.service.criticalText").value(serviceODTO.getCriticalText()))
                .andExpect(jsonPath("$.data.service.resources[0].criticalText")
                        .value(serviceODTO.getResources().get(0).getCriticalText()))
                .andExpect(jsonPath("$.data.service.resources[0].owners[0].criticalText")
                        .value(serviceODTO.getResources().get(0).getOwners().get(0).getCriticalText()));
        MvcResult mvcResult = resultActions.andReturn();
        HttpResponse httpResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                HttpResponse.class
        );
        ServiceO serviceO = objectMapper.convertValue(
                httpResponse.getData().get("service"),
                new TypeReference<>() {
                }
        );

        Optional<ServiceODTONoID> serviceODTOSaved =
                dTOMapper.mapOptional(serviceRepository.findById(serviceO.getId()), ServiceODTONoID.class);
        ServiceODTONoID serviceODTONoID =
                dTOMapper.map(serviceODTO, ServiceODTONoID.class);
        assertThat(serviceODTOSaved.isPresent()).isTrue();
        assertThat(serviceODTOSaved.get()).isEqualTo(serviceODTONoID);
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

        mockMvc.perform(post(serviceEndpoint)
                        .with(authorities)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceO)))
                .andExpect(status().isCreated());

        MvcResult getServicesResult = mockMvc.perform(get(serviceEndpoint + "/all").
                        with(authorities)
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
                .perform(delete(serviceEndpoint)
                        .with(authorities)
                        .param("id", id));

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
                .perform(delete(serviceEndpoint)
                        .with(authorities)
                        .param("id", id));
        // then
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Not Found"));
    }

    @Test
    void canEditService() throws Exception {
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
                "newCriticalText0001",
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

        mockMvc.perform(post(serviceEndpoint)
                        .with(authorities)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceO)))
                .andExpect(status().isCreated());

        MvcResult getServicesResult = mockMvc.perform(get(serviceEndpoint + "/all")
                        .with(authorities)
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
                .perform(put(serviceEndpoint).param("id", id)
                        .with(authorities)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newService))
                );

        // then
        resultActions.andExpect(status().isOk());
        Optional<ServiceO> extractedService = serviceRepository.findById(id);
        assertThat(extractedService.isPresent()).isTrue();
        assertThat(extractedService.get().getCriticalText()).isEqualTo(newService.getCriticalText());
        ServiceODTONoID extractedServiceNoId =
                dTOMapper.map(extractedService.get(), ServiceODTONoID.class);
        ServiceODTONoID newServiceNoID =
                dTOMapper.map(newService, ServiceODTONoID.class);
        assertThat(extractedServiceNoId).isEqualTo(newServiceNoID);


    }

    @Test
    void ThrowWhenEditServiceNotFound() throws Exception {
        // given
        String id = "id";
        ServiceO newService = ServiceO.builder().build();
        // when
        ResultActions resultActions = mockMvc
                .perform(put(serviceEndpoint).param("id", id)
                        .with(authorities)
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