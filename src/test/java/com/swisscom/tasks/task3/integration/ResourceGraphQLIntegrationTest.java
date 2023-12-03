package com.swisscom.tasks.task3.integration;

import com.swisscom.tasks.task3.configuration.DTOMapperBean;
import com.swisscom.tasks.task3.dto.mapper.DTOMapper;
import com.swisscom.tasks.task3.dto.resource.ResourceDTONoID;
import com.swisscom.tasks.task3.dto.service.ServiceODTONoID;
import com.swisscom.tasks.task3.model.Resource;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.model.auth.LoginRequest;
import com.swisscom.tasks.task3.model.auth.LoginResponseDTO;
import com.swisscom.tasks.task3.service.AuthenticationService;
import com.swisscom.tasks.task3.service.ResourceService;
import com.swisscom.tasks.task3.service.ServiceOService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResourceGraphQLIntegrationTest {
    @Autowired
    ResourceService resourceService;
    private HttpGraphQlTester graphQlTester;
    @Autowired
    private DTOMapper dtoMapper;
    @Autowired
    private AuthenticationService authenticationService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        resourceService.deleteAll();
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
    void shouldReturnResource(){
        //given
        // language=GraphQL
        String mutation1 = """
            mutation createService($service: ServiceInput!) {
                createService(service: $service){
                    id
                    criticalText
                 }
            }
        """;
        ServiceO service = ServiceO.builder()
                .criticalText("criticalText")
                .build();
        ServiceO savedService = graphQlTester.document(mutation1)
                .variable("service", dtoMapper.map(service, ServiceODTONoID.class))
                .execute()
                .path("createService")
                .entity(ServiceO.class)
                .satisfies(s -> {
                    assertEquals(service.getCriticalText(), s.getCriticalText());
                }).get();
        assertNotNull(savedService.getId());
        // language=GraphQL
        String mutation = """
            mutation createResource($resource: ResourceInput!, $id: ID!) {
                createResource(resource: $resource, serviceId: $id){
                    id
                    criticalText
                 }
            }
        """;
        //when
        Resource resource= Resource.builder().criticalText("resourceCriticalText")
                .owners(List.of())
                .build();


        Resource resource1 = graphQlTester.document(mutation)
                .variable("resource", dtoMapper.map(resource, ResourceDTONoID.class))
                .variable("id", savedService.getId())
                .execute()
                .path("createResource")
                .entity(Resource.class)
                .satisfies(s -> {
                    assertEquals(resource.getCriticalText(), s.getCriticalText());
                }).get();

        // language=GraphQL
        String document = """
            query($id: ID!) {
              resource(id: $id){
                id
                criticalText
              }
            }
        """;
        //when
        graphQlTester.document(document)
                .variable("id", resource1.getId())
                .execute()
                .path("resource")
                .entity(Resource.class)
                .satisfies(s -> assertEquals(resource.getCriticalText(), s.getCriticalText()));
    }

    @Test
    void findAllShouldNotReturnAllResources(){
        // language=GraphQL
        String document = """
            query {
                resources{
                    id
                    criticalText
                }
            }
        """;
        graphQlTester.document(document)
                .execute()
                .path("resources")
                .entityList(Resource.class)
                .hasSize(0);
    }
    @Test
    void findAllShouldReturnAllResources(){
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
                                .resources(List.of(Resource.builder()
                                        .criticalText("resourceCriticalText")
                                        .owners(List.of())
                                        .build()))
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
                resources{
                    id
                    criticalText
                }
            }
        """;
        graphQlTester.document(document)
                .execute()
                .path("resources")
                .entityList(Resource.class)
                .hasSize(1)
                .satisfies(s -> {
                    assertEquals("resourceCriticalText", s.get(0).getCriticalText());
                });
        ;
    }
    @Test
    void shouldUpdateResource() {
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
                .resources(
                        List.of(
                                Resource.builder()
                                        .criticalText("resourceCriticalText")
                                        .owners(List.of())
                                        .build()
                        )
                )
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
                    mutation updateResource($id: ID!, $resource: ResourceInput!) {
                        updateResource(id: $id, resource: $resource){
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
                """;
        Resource updatedResource = Resource.builder()
                .criticalText("updatedCriticalTextResource")
                .build();
        Resource updatedSavedResource = graphQlTester.document(updateMutation)
                .variable("id", savedService.getResources().get(0).getId())
                .variable("resource", dtoMapper.map(updatedResource, ResourceDTONoID.class))
                .execute()
                .path("updateResource")
                .entity(Resource.class)
                .satisfies(s -> {
                    assertEquals(updatedResource.getCriticalText(), s.getCriticalText());
                })
                .get();
        // language=GraphQL
        String document = """
                    query($id: ID!) {
                      resource(id: $id){
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
                """;
        //when
        graphQlTester.document(document)
                .variable("id", updatedSavedResource.getId())
                .execute()
                .path("resource")
                .entity(Resource.class)
                .satisfies(s -> {
                    assertEquals(updatedResource.getCriticalText(), s.getCriticalText());
                });
    }
    @Test
    void shouldDeleteResource() {
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
                .resources(
                        List.of(
                                Resource.builder()
                                        .criticalText("resourceCriticalText")
                                        .owners(List.of())
                                        .build()
                        )
                )
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
                    mutation deleteResource($id: ID!) {
                        deleteResource(id: $id){
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
                """;
        Resource deletedResource = graphQlTester.document(deleteMutation)
                .variable("id", savedService.getResources().get(0).getId())
                .execute()
                .path("deleteResource")
                .entity(Resource.class)
                .satisfies(s -> {
                    assertEquals(savedService.getResources().get(0).getId(), s.getId());
                })
                .get();
        // language=GraphQL
        String document = """
                    query($id: ID!) {
                        resource(id: $id){
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
                """;
        //when
        graphQlTester.document(document)
                .variable("id", deletedResource.getId())
                .execute()
                .path("resource")
                .valueIsNull();
        assertEquals(deletedResource.getId(), savedService.getResources().get(0).getId());
    }
}
