package com.kornasdominika.passwordwallet.model;

import java.io.Serializable;

/**
 * Class represents Password table into Database
 */
public class Password implements Serializable {

    public int pid;
    public String password;
    public int uid;
    public String webAddress;
    public String description;
    public String login;
    public int owner;
    public int mid;

    public Password(int pid, String password, int uid, String webAddress, String description, String login, int owner, int mid) {
        this.pid = pid;
        this.password = password;
        this.uid = uid;
        this.webAddress = webAddress;
        this.description = description;
        this.login = login;
        this.owner = owner;
        this.mid = mid;
    }

    public Password(String password, int uid, String webAddress, String description, String login, int owner, int mid) {
        this.password = password;
        this.uid = uid;
        this.webAddress = webAddress;
        this.description = description;
        this.login = login;
        this.owner = owner;
        this.mid = mid;
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

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    @Override
    public String toString() {
        return "Password{" +
                "pid=" + pid +
                ", password='" + password + '\'' +
                ", uid=" + uid +
                ", webAddress='" + webAddress + '\'' +
                ", description='" + description + '\'' +
                ", login='" + login + '\'' +
                ", owner=" + owner +
                ", mid=" + mid +
                '}';
    }
}
