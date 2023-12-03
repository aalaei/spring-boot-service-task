package com.swisscom.tasks.task3.integration;

import com.swisscom.tasks.task3.configuration.DTOMapperBean;
import com.swisscom.tasks.task3.dto.mapper.DTOMapper;
import com.swisscom.tasks.task3.dto.service.ServiceODTONoID;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.model.auth.LoginRequest;
import com.swisscom.tasks.task3.model.auth.LoginResponseDTO;
import com.swisscom.tasks.task3.service.AuthenticationService;
import com.swisscom.tasks.task3.service.ServiceOService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceGraphQLIntegrationTest {
    @Autowired
    ServiceOService serviceOService;
    private HttpGraphQlTester graphQlTester;
    @Autowired
    private DTOMapper dtoMapper;
    @Autowired
    private AuthenticationService authenticationService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        serviceOService.deleteAll();
        dtoMapper = new DTOMapper(new DTOMapperBean().modelMapper());
        LoginResponseDTO loginResponseDTO = authenticationService.loginUser(
                new LoginRequest("admin", "admin")
        );
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl(String.format("http://localhost:%s/graphql", port))
                .defaultHeader("Authorization", "Bearer " + loginResponseDTO.getJwt())
                .build();

        graphQlTester = HttpGraphQlTester.create(client);
    }
    @Test
    void contextLoads() {
        assertNotNull(graphQlTester);
    }
    @Test
    void shouldReturnService(){
        //given
        // language=GraphQL
        String mutation = """
            mutation createService($service: ServiceInput!) {
                createService(service: $service){
                    id
                    criticalText
                    resources{
                        id
                        criticalText
                        owners{
                            id
                            criticalText
                            name
                            level
                            accountNumber
                        }
                    }
                 }
            }
        """;
        ServiceO service = ServiceO.builder()
                .criticalText("criticalText")
                .build();
        ServiceO savedService = graphQlTester.document(mutation)
                .variable("service", dtoMapper.map(service, ServiceODTONoID.class))
                .execute()
                .path("createService")
                .entity(ServiceO.class)
                .satisfies(s -> {
                    assertEquals(service.getCriticalText(), s.getCriticalText());
                })
                .get();
        // language=GraphQL
        String document = """
            query($id: ID!) {
              service(id: $id){
                id
                criticalText
                resources{
                    id
                    criticalText
                    owners{
                        id
                        criticalText
                        name
                        level
                        accountNumber
                    }
                }
              }
            }
        """;
        //when
        graphQlTester.document(document)
                .variable("id", savedService.getId())
                .execute()
                .path("service")
                .entity(ServiceO.class)
                .satisfies(s -> {
                    assertEquals("criticalText", s.getCriticalText());
                });
    }

    @Test
    void findAllShouldNotReturnAllServices(){
        // language=GraphQL
        String document = """
            query {
                services{
                    id
                    criticalText
                }
            }
        """;
        graphQlTester.document(document)
                .execute()
                .path("services")
                .entityList(ServiceO.class)
                .hasSize(0);
    }
    @Test
    void findAllShouldReturnAllServices(){
        //given
        // language=GraphQL
        String mutation = """
            mutation createService($service: ServiceInput!) {
                createService(service: $service){
                    id
                    criticalText
                    resources{
                        id
                        criticalText
                        owners{
                            id
                            criticalText
                            name
                            level
                            accountNumber
                        }
                    }
                 }
            }
        """;
        graphQlTester.document(mutation)
                .variable("service", dtoMapper.map(ServiceO.builder()
                        .criticalText("criticalText")
                        .build(), ServiceODTONoID.class))
                .execute()
                .path("createService")
                .entity(ServiceO.class)
                .satisfies(s -> {
                    assertEquals("criticalText", s.getCriticalText());
                });
        // language=GraphQL
        String document = """
            query {
                services{
                    id
                    criticalText
                }
            }
        """;
        graphQlTester.document(document)
                .execute()
                .path("services")
                .entityList(ServiceO.class)
                .hasSize(1);
    }
    @Test
    void shouldUpdateService() {
        //given
        // language=GraphQL
        String mutation = """
                    mutation createService($service: ServiceInput!) {
                        createService(service: $service){
                            id
                            criticalText
                            resources{
                                id
                                criticalText
                                owners{
                                    id
                                    criticalText
                                    name
                                    level
                                    accountNumber
                                }
                            }
                         }
                    }
                """;
        ServiceO service = ServiceO.builder()
                .criticalText("criticalText")
                .build();
        ServiceO savedService = graphQlTester.document(mutation)
                .variable("service", dtoMapper.map(service, ServiceODTONoID.class))
                .execute()
                .path("createService")
                .entity(ServiceO.class)
                .satisfies(s -> {
                    assertEquals(service.getCriticalText(), s.getCriticalText());
                })
                .get();
        // language=GraphQL
        String updateMutation = """
                    mutation updateService($id: ID!, $service: ServiceInput!) {
                        updateService(id: $id, service: $service){
                            id
                            criticalText
                            resources{
                                id
                                criticalText
                                owners{
                                    id
                                    criticalText
                                    name
                                    level
                                    accountNumber
                                }
                            }
                         }
                    }
                """;
        ServiceO updatedService = ServiceO.builder()
                .criticalText("updatedCriticalText")
                .build();
        ServiceO updatedSavedService = graphQlTester.document(updateMutation)
                .variable("id", savedService.getId())
                .variable("service", dtoMapper.map(updatedService, ServiceODTONoID.class))
                .execute()
                .path("updateService")
                .entity(ServiceO.class)
                .satisfies(s -> {
                    assertEquals(updatedService.getCriticalText(), s.getCriticalText());
                })
                .get();
        // language=GraphQL
        String document = """
                    query($id: ID!) {
                      service(id: $id){
                        id
                        criticalText
                        resources{
                            id
                            criticalText
                            owners{
                                id
                                criticalText
                                name
                                level
                                accountNumber
                            }
                        }
                      }
                    }
                """;
        //when
        graphQlTester.document(document)
                .variable("id", updatedSavedService.getId())
                .execute()
                .path("service")
                .entity(ServiceO.class)
                .satisfies(s -> {
                    assertEquals("updatedCriticalText", s.getCriticalText());
                });
    }
    @Test
    void shouldDeleteService() {
        //given
        // language=GraphQL
        String mutation = """
                    mutation createService($service: ServiceInput!) {
                        createService(service: $service){
                            id
                            criticalText
                            resources{
                                id
                                criticalText
                                owners{
                                    id
                                    criticalText
                                    name
                                    level
                                    accountNumber
                                }
                            }
                         }
                    }
                """;
        ServiceO service = ServiceO.builder()
                .criticalText("criticalText")
                .build();
        ServiceO savedService = graphQlTester.document(mutation)
                .variable("service", dtoMapper.map(service, ServiceODTONoID.class))
                .execute()
                .path("createService")
                .entity(ServiceO.class)
                .satisfies(s -> {
                    assertEquals(service.getCriticalText(), s.getCriticalText());
                })
                .get();
        // language=GraphQL
        String deleteMutation = """
                    mutation deleteService($id: ID!) {
                        deleteService(id: $id){
                            id
                            criticalText
                            resources{
                                id
                                criticalText
                                owners{
                                    id
                                    criticalText
                                    name
                                    level
                                    accountNumber
                                }
                            }
                         }
                    }
                """;
        ServiceO deletedService = graphQlTester.document(deleteMutation)
                .variable("id", savedService.getId())
                .execute()
                .path("deleteService")
                .entity(ServiceO.class)
                .satisfies(s -> {
                    assertEquals(savedService.getId(), s.getId());
                })
                .get();
        // language=GraphQL
        String document = """
                    query($id: ID!) {
                      service(id: $id){
                        id
                        criticalText
                        resources{
                            id
                            criticalText
                            owners{
                                id
                                criticalText
                                name
                                level
                                accountNumber
                            }
                        }
                      }
                    }
                """;
        //when
        graphQlTester.document(document)
                .variable("id", deletedService.getId())
                .execute()
                .path("service")
                .valueIsNull();
        assertEquals(deletedService.getId(), savedService.getId());
    }
}
