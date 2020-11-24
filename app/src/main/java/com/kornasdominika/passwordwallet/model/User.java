package com.kornasdominika.passwordwallet.model;

/**
 * Class represents User table into Database
 */
public class User {

    public int uid;
    public String login;
    public String passwordHash;
    public String salt;
    public boolean isHash;

    public User(String login, String passwordHash, String salt, boolean isHash) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.isHash = isHash;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isHash() {
        return isHash;
    }

    public void setHash(boolean hash) {
        isHash = hash;
    }

}

