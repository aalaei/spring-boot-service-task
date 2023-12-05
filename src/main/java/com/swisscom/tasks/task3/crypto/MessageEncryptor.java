package com.swisscom.tasks.task3.crypto;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Provides encryption and decryption for DTO mappers.
 */
@Component
public class MessageEncryptor {
    private final EncryptionUtil encryptionUtil;
    private final boolean enabled;

    public MessageEncryptor(Environment environment) {
        String key = environment.getProperty("application.security.encryption.dto.key",
                "NGHJJjWm+gp/lmJ4lX3JOA==");
        String initVector = environment.getProperty("application.security.encryption.dto.initVector",
                "K869pc8rp6oSPQwJVGvM/Q==");
        String algo = environment.getProperty("application.security.encryption.dto.algo",
                "AES/CBC/PKCS5PADDING");
        enabled = Boolean.parseBoolean(
                environment.getProperty("application.security.encryption.dto.enabled", "true")
        );
        this.encryptionUtil = new EncryptionUtil(key, initVector, algo);
    }

    public String encrypt(String value) {
        return enabled ? encryptionUtil.encrypt(value) : value;
    }

    public String decrypt(String encrypted) {
        return enabled ? encryptionUtil.decrypt(encrypted) : encrypted;
    }
}
