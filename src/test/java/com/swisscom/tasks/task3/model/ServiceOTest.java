package com.swisscom.tasks.task3.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
class ServiceOTest {
    @Test
    void getId() {
        List<Owner> owners1= List.of(
                Owner.builder()
                        .id("id1")
                        .criticalText("criticalText11")
                        .name("name1")
                        .accountNumber("accountNumber1")
                        .level(1)
                        .build()
                ,
                Owner.builder()
                        .id("id2")
                        .criticalText("criticalText12")
                        .name("name2")
                        .accountNumber("accountNumber2")
                        .level(2)
                        .build()
        );
        List<Owner> owners2= List.of(
                Owner.builder()
                        .id("id3")
                        .criticalText("criticalText21")
                        .name("name3")
                        .accountNumber("accountNumber3")
                        .level(3)
                        .build()
                ,
                Owner.builder()
                        .id("id4")
                        .criticalText("criticalText13")
                        .name("name4")
                        .accountNumber("accountNumber14")
                        .level(4)
                        .build()
        );
        List<Resource> responses = List.of(
                Resource.builder()
                        .id("id1")
                        .criticalText("criticalText1")
                        .owners(owners1)
                        .build()
                ,
                Resource.builder()
                        .id("id2")
                        .criticalText("criticalText2")
                        .owners(owners2)
                        .build()
        );

        ServiceO serviceO = ServiceO.builder()
                .id("id")
                .criticalText("criticalText")
                .resources(responses)
                .build();
        // Convert to JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(serviceO);
            ServiceO serviceParsed = mapper.readValue(jsonString, ServiceO.class);
            assertEquals(serviceO, serviceParsed);
            assertNotNull(serviceParsed.getResources().get(1).getOwners().get(0));
            System.out.println(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void ShouldEqualCriticalText() {
        ServiceO serviceO= ServiceO.builder()
                .id("id")
                .criticalText("criticalText")
                .build();
        assertEquals("criticalText", serviceO.getCriticalText());
    }

    @Test
    void shouldEqualAllCriticalTexts() {
        ServiceO serviceO = ServiceO.builder()
                .id("id")
                .criticalText("criticalText1")
                .resources(List.of(
                        Resource.builder()
                                .id("id")
                                .criticalText("criticalText2")
                                .owners(List.of(
                                        Owner.builder()
                                                .id("id")
                                                .criticalText("criticalText3")
                                                .name("name")
                                                .accountNumber("accountNumber")
                                                .level(1)
                                                .build()
                                ))
                                .build()
                )).build();
        assertEquals("criticalText1", serviceO.getCriticalText());
        assertEquals("criticalText2", serviceO.getResources().get(0).getCriticalText());
        assertEquals("criticalText3", serviceO.getResources().get(0).getOwners().get(0).getCriticalText());
    }

}