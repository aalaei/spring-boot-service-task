package com.swisscom.tasks.task3.crypto;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MessageEncryptor {
    private final EncryptionUtil encryptionUtil;

    public MessageEncryptor(Environment environment) {
        String key  = environment.getProperty("encryption.key", "MTIzNDU2NzgK");
        String initVector = environment.getProperty("encryption.initVector",
                "MTIzNDU2NzgxMjM0NTY3OAo=");
        String algo = environment.getProperty("encryption.algo", "AES/CBC/PKCS5PADDING");
        this.encryptionUtil = new EncryptionUtil(key, initVector, algo);
    }

    public String encrypt(String value) {
        return encryptionUtil.encrypt(value);
    }
    public String decrypt(String encrypted) {
        return encryptionUtil.decrypt(encrypted);
    }
}
