package com.swisscom.tasks.task3.integration;

import com.swisscom.tasks.task3.configuration.DTOMapperBean;
import com.swisscom.tasks.task3.dto.owner.OwnerDTO;
import com.swisscom.tasks.task3.dto.mapper.DTOMapper;
import com.swisscom.tasks.task3.dto.resource.ResourceDTONoID;
import com.swisscom.tasks.task3.dto.service.ServiceODTONoID;
import com.swisscom.tasks.task3.model.Owner;
import com.swisscom.tasks.task3.model.Resource;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.dto.auth.LoginRequestDTO;
import com.swisscom.tasks.task3.dto.auth.LoginResponseDTO;
import com.swisscom.tasks.task3.service.AuthenticationService;
import com.swisscom.tasks.task3.service.OwnerService;
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
public class OwnerGraphQLIntegrationTest {
    @Autowired
    OwnerService ownerService;
    private HttpGraphQlTester graphQlTester;
    @Autowired
    private DTOMapper dtoMapper;
    @Autowired
    private AuthenticationService authenticationService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        ownerService.deleteAll();
        dtoMapper = new DTOMapper(new DTOMapperBean().modelMapper());
        LoginResponseDTO loginResponseDTO = authenticationService.loginUser(
                new LoginRequestDTO("admin", "admin")
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
    void shouldReturnOwner(){
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
        String mutation2= """
            mutation createOwner($owner: OwnerInput!, $id: ID!) {
                createOwner(owner: $owner, resourceId: $id){
                    id
                    criticalText
                 }
            }
        """;
        Owner owner = Owner.builder()
                .criticalText("ownerCriticalText")
                .name("ownerName")
                .accountNumber("ownerAccountNumber")
                .level(1)
                .build();
        Owner owner1 = graphQlTester.document(mutation2)
                .variable("owner", dtoMapper.map(owner, OwnerDTO.class))
                .variable("id", resource1.getId())
                .execute()
                .path("createOwner")
                .entity(Owner.class)
                .satisfies(s -> {
                    assertEquals(owner.getCriticalText(), s.getCriticalText());
                }).get();


        // language=GraphQL
        String document = """
            query($id: ID!) {
              owner(id: $id){
                id
                criticalText
              }
            }
        """;
        //when
        graphQlTester.document(document)
                .variable("id", owner1.getId())
                .execute()
                .path("owner")
                .entity(Owner.class)
                .satisfies(s -> assertEquals(owner.getCriticalText(), s.getCriticalText()));
    }

    @Test
    void findAllShouldNotReturnAllOwners(){
        // language=GraphQL
        String document = """
            query {
                owners{
                    id
                    criticalText
                }
            }
        """;
        graphQlTester.document(document)
                .execute()
                .path("owners")
                .entityList(Owner.class)
                .hasSize(0);
    }
    @Test
    void findAllShouldReturnAllOwners(){
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
                                        .owners(List.of(
                                                Owner.builder()
                                                        .criticalText("ownerCriticalText")
                                                        .name("ownerName")
                                                        .accountNumber("ownerAccountNumber")
                                                        .level(1)
                                                        .build()
                                        ))
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
                owners{
                    id
                    criticalText
                }
            }
        """;
        graphQlTester.document(document)
                .execute()
                .path("owners")
                .entityList(Owner.class)
                .hasSize(1)
                .satisfies(s -> {
                    assertEquals("ownerCriticalText", s.get(0).getCriticalText());
                });
    }
    @Test
    void shouldUpdateOwner() {
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
                                        .owners(List.of(
                                                Owner.builder()
                                                        .criticalText("ownerCriticalText")
                                                        .name("ownerName")
                                                        .accountNumber("ownerAccountNumber")
                                                        .level(1)
                                                        .build()
                                        ))
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
                    mutation updateOwner($id: ID!, $owner: OwnerInput!) {
                        updateOwner(id: $id, owner: $owner){
                            id
                            criticalText
                            name
                            level
                            accountNumber
                         }
                    }
                """;
        Owner updatedOwner = Owner.builder()
                .criticalText("updatedCriticalTextResource")
                .build();
        Owner updatedSavedOwner = graphQlTester.document(updateMutation)
                .variable("id", savedService.getResources().get(0).getOwners().get(0).getId())
                .variable("owner", dtoMapper.map(updatedOwner, OwnerDTO.class))
                .execute()
                .path("updateOwner")
                .entity(Owner.class)
                .satisfies(s -> {
                    assertEquals(updatedOwner.getCriticalText(), s.getCriticalText());
                })
                .get();
        // language=GraphQL
        String document = """
                    query($id: ID!) {
                      owner(id: $id){
                        id
                        criticalText
                        name
                        level
                        accountNumber
                      }
                    }
                """;
        //when
        graphQlTester.document(document)
                .variable("id", updatedSavedOwner.getId())
                .execute()
                .path("owner")
                .entity(Owner.class)
                .satisfies(s -> {
                    assertEquals(updatedOwner.getCriticalText(), s.getCriticalText());
                });
    }
    @Test
    void shouldDeleteOwner() {
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
                                        .owners(List.of(
                                                Owner.builder()
                                                        .criticalText("ownerCriticalText")
                                                        .name("ownerName")
                                                        .accountNumber("ownerAccountNumber")
                                                        .level(1)
                                                        .build()
                                        ))
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
                    mutation deleteOwner($id: ID!) {
                        deleteOwner(id: $id){
                            id
                            criticalText
                            name
                            level
                            accountNumber
                        }
                    }
                """;
        Owner deletedOwner = graphQlTester.document(deleteMutation)
                .variable("id", savedService.getResources().get(0).getOwners().get(0).getId())
                .execute()
                .path("deleteOwner")
                .entity(Owner.class)
                .satisfies(s -> {
                    assertEquals(savedService.getResources().get(0).getOwners().get(0).getId(), s.getId());
                })
                .get();
        // language=GraphQL
        String document = """
                    query($id: ID!) {
                        owner(id: $id){
                            id
                            criticalText
                            name
                            level
                            accountNumber
                        }
                    }
                """;
        //when
        graphQlTester.document(document)
                .variable("id", deletedOwner.getId())
                .execute()
                .path("owner")
                .valueIsNull();
        assertEquals(deletedOwner.getId(), savedService.getResources().get(0).getOwners().get(0).getId());
    }
}
