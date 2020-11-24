package com.kornasdominika.passwordwallet.encryption;

import java.security.SecureRandom;

/**
 * Class contains method responsible for generate random salt for SHA512 algorithm or key for HMAC algorithm
 */
public class EncryptionKey {

    public static String generateKey(int numberOfBytes){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[numberOfBytes];
        random.nextBytes(bytes);
        return new String(bytes);
    }
}
