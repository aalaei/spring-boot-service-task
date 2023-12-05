package com.swisscom.tasks.task3client.crypto.service;

import com.swisscom.tasks.task3client.crypto.EncryptionUtil;
import com.swisscom.tasks.task3client.model.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OwnerEncryptor {

    private final EncryptionUtil messageEncryptor;

    public Owner encrypt(Owner owner) {
        if (owner.getCriticalText() != null)
            owner.setCriticalText(messageEncryptor.encrypt(owner.getCriticalText()));
        return owner;
    }

    public Owner decrypt(Owner resource) {
        if (resource.getCriticalText() != null)
            resource.setCriticalText(messageEncryptor.decrypt(resource.getCriticalText()));
        return resource;
    }
}
