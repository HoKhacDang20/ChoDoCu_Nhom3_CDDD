package com.example.chodocu_ver1.data_models;

public class TaikhoanNganHang {
    private String sTKID, sTenChuTK, sSoTK, sTenNH;
    private long lMoney;

    public TaikhoanNganHang() {
    }

    public TaikhoanNganHang(String sTKID, String sTenChuTK, String sSoTK, String sTenNH, long lMoney) {
        this.sTKID = sTKID;
        this.sTenChuTK = sTenChuTK;
        this.sSoTK = sSoTK;
        this.sTenNH = sTenNH;
        this.lMoney = lMoney;
    }

    public String getsTKID() {
        return sTKID;
    }

    public void setsTKID(String sTKID) {
        this.sTKID = sTKID;
    }

    public String getsTenChuTK() {
        return sTenChuTK;
    }

    public void setsTenChuTK(String sTenChuTK) {
        this.sTenChuTK = sTenChuTK;
    }

    public String getsSoTK() {
        return sSoTK;
    }

    public void setsSoTK(String sSoTK) {
        this.sSoTK = sSoTK;
    }

    public String getsTenNH() {
        return sTenNH;
    }

    public void setsTenNH(String sTenNH) {
        this.sTenNH = sTenNH;
    }

    public long getlMoney() {
        return lMoney;
    }

    public void setlMoney(long lMoney) {
        this.lMoney = lMoney;
    }
}
