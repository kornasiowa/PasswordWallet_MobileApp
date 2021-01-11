package com.kornasdominika.passwordwallet.model;

public class Function {

    public int fid;
    public int uid;
    public String time;
    public String funName;

    public Function(int uid, String time, String funName) {
        this.uid = uid;
        this.time = time;
        this.funName = funName;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

}
