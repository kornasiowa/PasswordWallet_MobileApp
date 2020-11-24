package com.kornasdominika.passwordwallet.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Class contains method responsible for create password hash with SHA512 algorithm and pepper value
 */
public class SHA512 {

    public static String pepper = "bWUfYieFGGRwdWZy7";

    public static String calculateSHA512(String text) {
        try {
            //get an instance of SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            //calculate message digest of the input string - returns byte array
            byte[] messageDigest = md.digest(text.getBytes());
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
            // Convert message digest into hex value
            String hashText = no.toString(16);
            // Add preceding 0s to make it 32 bit
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            // return the HashText
            return hashText;
        }
        // If wrong message digest algorithm was specified
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
