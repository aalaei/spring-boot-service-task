package com.swisscom.tasks.task3.configuration;

import com.swisscom.tasks.task3.crypto.DataBaseEncryptor;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

/**
 * MongoDBEventListener is a Configuration class for database.
 * It watches every data move and adds a protective layer by encrypting and decrypting,
 * keeping secret information safe and sound in MongoDB.
 */
@RequiredArgsConstructor
@Configuration
public class MongoDBEventListener extends AbstractMongoEventListener<Object> {

    private final DataBaseEncryptor encryptionUtil;
    @Value("${db.encryption.enabled:false}")
    private boolean encEnabled;

    @Override
    public void onAfterLoad(AfterLoadEvent<Object> event) {

        Document eventObject = event.getDocument();

        if (eventObject == null || !encEnabled) {
            return;
        }
        for (String key : eventObject.keySet()) {
            if (key.equals("criticalText")) {
                eventObject.put(key, encryptionUtil.decrypt(eventObject.get(key).toString()));
            }
        }
        super.onAfterLoad(event);
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<Object> event) {
        Document eventObject = event.getDocument();
        if (eventObject == null || !encEnabled) {
            return;
        }
        for (String key : eventObject.keySet()) {
            if (key.equals("criticalText")) {
                eventObject.put(key, encryptionUtil.encrypt(eventObject.get(key).toString()));
            }
        }

        super.onBeforeSave(event);
    }
}
