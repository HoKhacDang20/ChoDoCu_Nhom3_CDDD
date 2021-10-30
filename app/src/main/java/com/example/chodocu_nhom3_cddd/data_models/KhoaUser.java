package com.example.chodocu_nhom3_cddd.data_models;

public class KhoaUser {
    String khoaID, userID;

    public KhoaUser() {
    }

    public KhoaUser(String khoaID, String userID) {
        this.khoaID = khoaID;
        this.userID = userID;
    }

    public String getKhoaID() {
        return khoaID;
    }

    public void setKhoaID(String khoaID) {
        this.khoaID = khoaID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
