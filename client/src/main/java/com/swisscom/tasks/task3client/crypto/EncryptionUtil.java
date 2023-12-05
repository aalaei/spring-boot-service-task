package com.swisscom.tasks.task3client.crypto;

import com.swisscom.tasks.task3client.exception.EncryptionException;
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
    private Cipher getCipher(){
        try {
            IvParameterSpec iv = new IvParameterSpec(Base64.decodeBase64(initVector));
            SecretKeySpec sKeySpec = new SecretKeySpec(Base64.decodeBase64(key), algo.split("/")[0]);
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);
            return cipher;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptionException("Error while parsing key and IV");
        }
    }

    public String encrypt(String value) {
        if(!enabled)
            return value;
        try {
            return Base64.encodeBase64String(getCipher().doFinal(value.getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EncryptionException("Error while encrypting the message");
        }
    }

    public String decrypt(String encrypted) {
        if(!enabled)
            return encrypted;
        try {
            return new String(getCipher().doFinal(Base64.decodeBase64(encrypted)));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EncryptionException("Error while decrypting the message");
        }
    }
}