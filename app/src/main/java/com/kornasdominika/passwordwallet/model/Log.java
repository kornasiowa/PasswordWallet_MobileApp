package com.kornasdominika.passwordwallet.model;

public class Log {

    public int lid;
    public int uid;
    public String ip;
    public String time;
    public boolean isLastSuccess;
    public int failedAttempts;

    public Log(int uid, String ip, String time, boolean isLastSuccess, int failedAttempts) {
        this.uid = uid;
        this.ip = ip;
        this.time = time;
        this.isLastSuccess = isLastSuccess;
        this.failedAttempts = failedAttempts;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isLastSuccess() {
        return isLastSuccess;
    }

    public void setLastSuccess(boolean lastSuccess) {
        isLastSuccess = lastSuccess;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }
}
