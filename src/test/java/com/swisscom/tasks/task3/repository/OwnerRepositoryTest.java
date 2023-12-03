package com.swisscom.tasks.task3.repository;

import com.swisscom.tasks.task3.model.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import static org.junit.jupiter.api.Assertions.*;

//@Testcontainers

@DataMongoTest
class OwnerRepositoryTest {
    //    @Container
//    @ServiceConnection
//    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo");
    @Autowired
    private OwnerRepository ownerRepository;

    @AfterEach
    void tearDown() {
        ownerRepository.deleteAll();
    }

    @Test
    void itShouldExists() {
        ownerRepository.save(Owner.builder().accountNumber("AC001")
                .id("5")
                .criticalText("criticalText")
                .name("name")
                .level(1)
                .build());
        assertTrue(ownerRepository.existsById("5"));
    }

    @Test
    void itShouldNotExist() {
        assertFalse(ownerRepository.existsById("5"));
    }
}