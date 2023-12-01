package com.swisscom.tasks.task3.service.impl;

import com.swisscom.tasks.task3.exception.ServiceOServiceException;
import com.swisscom.tasks.task3.model.Owner;
import com.swisscom.tasks.task3.model.Resource;
import com.swisscom.tasks.task3.model.ServiceO;
import com.swisscom.tasks.task3.repository.OwnerRepository;
import com.swisscom.tasks.task3.repository.ResourceRepository;
import com.swisscom.tasks.task3.repository.ServiceORepository;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ServiceOServiceImplTest {
    @Mock
    private ServiceORepository serviceORepository;
    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private OwnerRepository ownerRepository;

    private ServiceOServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new ServiceOServiceImpl(serviceORepository, resourceRepository, ownerRepository);
    }

    @Test
    void canGetAllServices() {
        // when
        underTest.getAllDetailed();
        // then
        verify(serviceORepository).findAll();
    }
    @Test
    void canAddService() {
        // given
        ServiceO serviceO = ServiceO.builder()
                .criticalText("criticalText")
                .resources(List.of(
                        Resource.builder()
                                .criticalText("criticalText")
                                .owners(List.of(
                                        Owner.builder()
                                                .criticalText("criticalText")
                                                .name("name")
                                                .accountNumber("accountNumber")
                                                .level(1)
                                                .build()
                                ))
                                .build()
                )
                ).build();
        // when
        underTest.create(serviceO);
        // then
        ArgumentCaptor<ServiceO> serviceArgumentCaptor =
                ArgumentCaptor.forClass(ServiceO.class);

        verify(serviceORepository)
                .save(serviceArgumentCaptor.capture());

        ServiceO capturedService = serviceArgumentCaptor.getValue();

        assertThat(capturedService).isEqualTo(serviceO);
    }

    @Test
    void willThrowWhenAddServiceWithDuplicateId() {
        // given
        String id="id";
        ServiceO serviceO = ServiceO.builder().id(id).build();
        given(serviceORepository.existsById(id))
                .willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> underTest.create(serviceO))
                .isInstanceOf(ServiceOServiceException.class)
                .hasMessageContaining("Another service with id "+id+" exists before");
        verify(serviceORepository, never()).save(any());
        verify(resourceRepository, never()).save(any());
        verify(ownerRepository, never()).save(any());
    }
    @Test
    void canDeleteService() {
        // given
        String id = "id";
        given(serviceORepository.findById(id))
                .willReturn(Optional.of(ServiceO.builder()
                                .resources(List.of(
                                        Resource.builder()
                                                .owners(List.of(
                                                        Owner.builder()
                                                                .id("id")
                                                                .level(1)
                                                                .name("name")
                                                                .build()
                                                ))
                                                .build()
                                ))
                        .id(id)
                        .build()));
        // when
        underTest.deleteById(id);
        // then
        verify(serviceORepository).deleteById(id);
        verify(resourceRepository).deleteById(any());
        verify(ownerRepository).deleteById("id");
    }

    @Test
    void willThrowWhenDeleteServiceNotFound() {
        // given
        String id = "id";
        given(serviceORepository.findById(id))
                .willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> underTest.deleteById(id))
                .isInstanceOf(ServiceOServiceException.class)
                .hasMessageContaining("Service with id " + id + " does not exists");
        verify(serviceORepository, never()).deleteById(any());
        verify(resourceRepository, never()).deleteById(any());
        verify(ownerRepository, never()).deleteById(any());
    }
    @Test
    void canEditService() {
        // given
        String id = "id";
        ServiceO newServiceO = ServiceO.builder()
                .criticalText("newCriticalText1")
                .resources(List.of(
                        Resource.builder()
                                .criticalText("newCriticalText2")
                                .owners(List.of(
                                        Owner.builder()
                                                .criticalText("newCriticalText3")
                                                .name("newName")
                                                .accountNumber("newAccountNumber")
                                                .level(1)
                                                .build()
                                ))
                                .build()
                )
                ).build();
        given(serviceORepository.existsById(id))
                .willReturn(true);
        given(serviceORepository.save(newServiceO))
                .willReturn(newServiceO);
        given(resourceRepository.save(any()))
                .willReturn(newServiceO.getResources().get(0));
        given(ownerRepository.save(any()))
                .willReturn(newServiceO.getResources().get(0).getOwners().get(0));
        // when
        underTest.updateById(id, newServiceO);
        // then
        verify(serviceORepository).save(newServiceO);
        verify(resourceRepository).save(newServiceO.getResources().get(0));
        verify(ownerRepository).save(newServiceO.getResources().get(0).getOwners().get(0));
    }
    @Test
    void willThrowWhenEditServiceNotFound() {
        // given
        String id = "id";
        given(serviceORepository.existsById(id))
                .willReturn(false);
        // when
        // then
        assertThatThrownBy(() -> underTest.updateById(id, any()))
                .isInstanceOf(ServiceOServiceException.class)
                .hasMessageContaining("Service with id " + id + " does not exists");
        verify(serviceORepository, never()).save(any());
        verify(resourceRepository, never()).save(any());
        verify(ownerRepository, never()).save(any());
    }
}
