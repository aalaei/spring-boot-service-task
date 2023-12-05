package com.swisscom.tasks.task3.crypto;

import com.swisscom.tasks.task3.exception.EncryptionException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Provide encryption and decryption functionality.
 */
@RequiredArgsConstructor
public class EncryptionUtil {

    private final String key;
    private final String initVector;
    private final String algo;

    public String encrypt(String value) {
        try {
            return Base64.encodeBase64String(getCipher().doFinal(value.getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EncryptionException("Error while encrypting the message");
        }
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

    public String decrypt(String encrypted) {
        try {
            return new String(getCipher().doFinal(Base64.decodeBase64(encrypted)));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EncryptionException("Error while decrypting the message");
        }
    }
}