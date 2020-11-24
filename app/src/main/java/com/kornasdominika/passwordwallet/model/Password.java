package com.kornasdominika.passwordwallet.model;

/**
 * Class represents Password table into Database
 */
public class Password {

    public int pid;
    public String password;
    public int uid;
    public String webAddress;
    public String description;
    public String login;

    public Password(String password, int uid, String webAddress, String description, String login) {
        this.password = password;
        this.uid = uid;
        this.webAddress = webAddress;
        this.description = description;
        this.login = login;
    }

    public int getPid() {
        return pid;
    }

    public String getPassword() {
        return password;
    }

    public int getUid() {
        return uid;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public String getDescription() {
        return description;
    }

    public String getLogin() {
        return login;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
