package com.example.chodocu_ver1.data_models;

public class UserDepositData {
    private String idGiaoDich, userID;
    private long money;
    private int tinhTrang;

    public UserDepositData() {
    }

    public UserDepositData(String idGiaoDich, String userID, long money, int tinhTrang) {
        this.idGiaoDich = idGiaoDich;
        this.userID = userID;
        this.money = money;
        this.tinhTrang = tinhTrang;
    }

    public String getIdGiaoDich() {
        return idGiaoDich;
    }

    public void setIdGiaoDich(String idGiaoDich) {
        this.idGiaoDich = idGiaoDich;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }
}
