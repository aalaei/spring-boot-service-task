package com.swisscom.tasks.task3client.crypto;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Component
public class EncryptionUtil {
    private final boolean enabled;
    private final String key;
    private final String initVector;
    private final String algo;
    public EncryptionUtil(Environment environment) {

        key = environment.getProperty("application.connection.security.encryption.key",
                "NGHJJjWm+gp/lmJ4lX3JOA==");
        initVector = environment.getProperty("application.connection.security.encryption.initVector",
                "K869pc8rp6oSPQwJVGvM/Q==");
        algo = environment.getProperty("application.connection.security.encryption.algo",
                "AES/CBC/PKCS5PADDING");
        enabled = Boolean.parseBoolean(
                environment.getProperty("application.connection.security.encryption.enabled",
                        "true")
        );
    }

    public String encrypt(String value) {
        if(!enabled)
            return value;
        try {
            IvParameterSpec iv = new IvParameterSpec(Base64.decodeBase64(initVector));
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decodeBase64(key), "AES");

            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String decrypt(String encrypted) {
        if(!enabled)
            return encrypted;
        try {
            IvParameterSpec iv = new IvParameterSpec(Base64.decodeBase64(initVector));
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decodeBase64(key), "AES");

            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}