package com.kornasdominika.passwordwallet.model;

public class DataChange {

    public int cid;
    public int uid;
    public int rid;
    public int fid;
    public String actionType;
    public String previousValue;
    public String presentValue;
    public String time;
    public int isRestored;

    public DataChange(int cid, int uid, int rid, int fid, String actionType, String previousValue, String presentValue, String time, int isRestored) {
        this.cid = cid;
        this.uid = uid;
        this.rid = rid;
        this.fid = fid;
        this.actionType = actionType;
        this.previousValue = previousValue;
        this.presentValue = presentValue;
        this.time = time;
        this.isRestored = isRestored;
    }

    public DataChange(int uid, int rid, int fid, String actionType, String previousValue, String presentValue, String time, int isRestored) {
        this.uid = uid;
        this.rid = rid;
        this.fid = fid;
        this.actionType = actionType;
        this.previousValue = previousValue;
        this.presentValue = presentValue;
        this.time = time;
        this.isRestored = isRestored;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getPresentValue() {
        return presentValue;
    }

    public void setPresentValue(String presentValue) {
        this.presentValue = presentValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int isRestored() {
        return isRestored;
    }

    public void setRestored(int restored) {
        isRestored = restored;
    }
}
