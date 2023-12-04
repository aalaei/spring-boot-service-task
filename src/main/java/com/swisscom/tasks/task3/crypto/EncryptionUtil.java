package com.swisscom.tasks.task3.crypto;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
@RequiredArgsConstructor
public class EncryptionUtil {

    private final String key;
    private final String initVector;
    private final String algo;
    public String encrypt(String value) {
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