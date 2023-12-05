package com.swisscom.tasks.task3.crypto.service;

import com.swisscom.tasks.task3.crypto.MessageEncryptor;
import com.swisscom.tasks.task3.model.Owner;
import com.swisscom.tasks.task3.model.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OwnerEncryptor {

    private final MessageEncryptor messageEncryptor;
    public Owner encrypt(Owner owner) {
        if(owner.getCriticalText()!=null)
            owner.setCriticalText(messageEncryptor.encrypt(owner.getCriticalText()));
        return owner;
    }
    public Owner decrypt(Owner resource) {
        if(resource.getCriticalText()!=null)
            resource.setCriticalText(messageEncryptor.decrypt(resource.getCriticalText()));
        return resource;
    }
}
