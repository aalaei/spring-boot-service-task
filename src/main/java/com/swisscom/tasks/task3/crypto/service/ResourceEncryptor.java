package com.swisscom.tasks.task3.crypto.service;

import com.swisscom.tasks.task3.crypto.MessageEncryptor;
import com.swisscom.tasks.task3.model.Resource;
import com.swisscom.tasks.task3.model.ServiceO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ResourceEncryptor {

    private final MessageEncryptor messageEncryptor;
    public Resource encrypt(Resource resource) {
        if(resource.getCriticalText()!=null)
            resource.setCriticalText(messageEncryptor.encrypt(resource.getCriticalText()));
        if(resource.getOwners()!=null){
            resource.getOwners().forEach(o->{
                if(o.getCriticalText()!=null)
                    o.setCriticalText(messageEncryptor.encrypt(o.getCriticalText()));
            });
        }
        return resource;
    }
    public Resource decrypt(Resource resource) {
        if(resource.getOwners()!=null){
            resource.getOwners().forEach(o->{
                if(o.getCriticalText()!=null)
                    o.setCriticalText(messageEncryptor.decrypt(o.getCriticalText()));
            });
        }
        if(resource.getCriticalText()!=null)
            resource.setCriticalText(messageEncryptor.decrypt(resource.getCriticalText()));
        return resource;
    }
}
