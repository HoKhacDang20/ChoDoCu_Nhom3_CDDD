package com.example.chodocu_nhom3_cddd.data_models;

public class CuaHang {
    private String shopID, userID, shopName, moTaShop, shopImage, ngayTaoShop;
    private int tinhTrangShop;

    public CuaHang() {
    }

    public CuaHang(String shopID, String userID, String shopName, String moTaShop, String shopImage, String ngayTaoShop,  int tinhTrangShop) {
        this.shopID = shopID;
        this.userID = userID;
        this.shopName = shopName;
        this.moTaShop = moTaShop;
        this.shopImage = shopImage;
        this.ngayTaoShop = ngayTaoShop;
        this.tinhTrangShop = tinhTrangShop;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getMoTaShop() {
        return moTaShop;
    }

    public void setMoTaShop(String moTaShop) {
        this.moTaShop = moTaShop;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getNgayTaoShop() {
        return ngayTaoShop;
    }

    public void setNgayTaoShop(String ngayTaoShop) {
        this.ngayTaoShop = ngayTaoShop;
    }

    public int getTinhTrangShop() {
        return tinhTrangShop;
    }

    public void setTinhTrangShop(int tinhTrangShop) {
        this.tinhTrangShop = tinhTrangShop;
    }
}
