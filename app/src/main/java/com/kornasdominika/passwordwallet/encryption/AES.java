package com.kornasdominika.passwordwallet.encryption;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class contains methods responsible for data encryption and decryption with AES algorithm
 */
public class AES {

    private static final String ALGO = "AES";

    //encrypts string and returns encrypted string
    public static String encrypt(String data, Key key) throws Exception {
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }
    //decrypts string and returns plain text
    public static String decrypt(String encryptedData, Key key) throws Exception {
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    //generate 128 bits key from User master password
    public Key generateKey(String masterPassword) throws Exception {
        byte[] massterPasswordArray = masterPassword.getBytes();
        return new SecretKeySpec(massterPasswordArray, 0, 16, ALGO);
    }
}
