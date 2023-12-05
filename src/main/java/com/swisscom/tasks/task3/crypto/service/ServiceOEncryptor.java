package com.swisscom.tasks.task3.crypto.service;

import com.swisscom.tasks.task3.crypto.MessageEncryptor;
import com.swisscom.tasks.task3.model.ServiceO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ServiceOEncryptor {

    private final MessageEncryptor messageEncryptor;

    public ServiceO encrypt(ServiceO serviceO) {
        if (serviceO.getCriticalText() != null)
            serviceO.setCriticalText(messageEncryptor.encrypt(serviceO.getCriticalText()));
        if (serviceO.getResources() != null) {
            serviceO.getResources().forEach(r -> {
                if (r.getCriticalText() != null)
                    r.setCriticalText(messageEncryptor.encrypt(r.getCriticalText()));
                if (r.getOwners() != null)
                    r.getOwners().forEach(o -> {
                        if (o.getCriticalText() != null)
                            o.setCriticalText(messageEncryptor.encrypt(o.getCriticalText()));
                    });
            });
        }
        return serviceO;
    }

    public ServiceO decrypt(ServiceO serviceO) {
        if (serviceO.getResources() != null) {
            serviceO.getResources().forEach(r -> {
                if (r.getCriticalText() != null)
                    r.setCriticalText(messageEncryptor.decrypt(r.getCriticalText()));
                if (r.getOwners() != null)
                    r.getOwners().forEach(o -> {
                        if (o.getCriticalText() != null)
                            o.setCriticalText(messageEncryptor.decrypt(o.getCriticalText()));
                    });
            });
        }
        if (serviceO.getCriticalText() != null)
            serviceO.setCriticalText(messageEncryptor.decrypt(serviceO.getCriticalText()));
        return serviceO;
    }
}
