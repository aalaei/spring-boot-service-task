package com.swisscom.tasks.task3.crypto;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DataBaseEncryptor {
    private final EncryptionUtil encryptionUtil;

    public DataBaseEncryptor(Environment environment) {
        String key  = environment.getProperty("db.encryption.key", "MTIzNDU2NzgK");
        String initVector = environment.getProperty("db.encryption.initVector",
                "MTIzNDU2NzgxMjM0NTY3OAo=");
        String algo = environment.getProperty("db.encryption.algo", "AES/CBC/PKCS5PADDING");
        this.encryptionUtil = new EncryptionUtil(key, initVector, algo);
    }

    public String encrypt(String value) {
        return encryptionUtil.encrypt(value);
    }
    public String decrypt(String encrypted) {
        return encryptionUtil.decrypt(encrypted);
    }
}