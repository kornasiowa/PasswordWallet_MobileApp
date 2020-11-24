package com.kornasdominika.passwordwallet.model;

/**
 * Class represents DTO object responsible for transfer User password hash information
 */
public class HashDTO {

    private String hash;
    private String key;
    private int isHash;

    public HashDTO(String hash, String key, int isHash) {
        this.hash = hash;
        this.key = key;
        this.isHash = isHash;
    }

    public String getHash() {
        return hash;
    }

    public String getKey() {
        return key;
    }

    public int isHash() {
        return isHash;
    }
}
