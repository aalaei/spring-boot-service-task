package com.swisscom.tasks.task3.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EncryptionUtilTest {
    EncryptionUtil encryptionUtil;

    @BeforeEach
    void setUp() {
        encryptionUtil = new EncryptionUtil("GSMVCiRrUVD2NBKgYGsg1A==",
                "xgKhU+NyQT5NOQvLqFwyQg==", "AES/CBC/PKCS5PADDING");
    }

    @Test
    void itShouldEncryptAndDecrypt() {
        String encrypted = encryptionUtil.encrypt("Hello World");
        String decrypted = encryptionUtil.decrypt(encrypted);
        assertEquals("5L1eY5g1wAhLyPVq+R/I5w==", encrypted);
        assertEquals("Hello World", decrypted);
    }

}