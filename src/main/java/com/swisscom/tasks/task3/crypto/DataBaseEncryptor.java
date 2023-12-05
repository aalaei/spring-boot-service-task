package com.swisscom.tasks.task3.crypto;

import com.swisscom.tasks.task3.configuration.MongoDBEventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Provides encryption and decryption of data as a service to be used in {@link MongoDBEventListener}.
 */
@Component
public class DataBaseEncryptor {
    private final EncryptionUtil encryptionUtil;

    public DataBaseEncryptor(Environment environment) {
        String key = environment.getProperty("application.security.encryption.db.key",
                "rYUwouQ16kswUnNYkdNDig==");
        String initVector = environment.getProperty("application.security.encryption.db.initVector",
                "0srIh8CGYrPESYZxZO9v1A==");
        String algo = environment.getProperty("application.security.encryption.db.algo",
                "AES/CBC/PKCS5PADDING");
        this.encryptionUtil = new EncryptionUtil(key, initVector, algo);
    }

    public String encrypt(String value) {
        return encryptionUtil.encrypt(value);
    }

    public String decrypt(String encrypted) {
        return encryptionUtil.decrypt(encrypted);
    }
}