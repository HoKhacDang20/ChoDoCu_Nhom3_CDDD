package com.example.chodocu_ver1.data_models;

public class HoaHong {
    private String id;
    private int userCommission;
    private int shopCommission;
    private int shipperCommission;

    public HoaHong() {
    }

    public HoaHong(String id, int userCommission, int shopCommission, int shipperCommission) {
        this.id = id;
        this.userCommission = userCommission;
        this.shopCommission = shopCommission;
        this.shipperCommission = shipperCommission;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserCommission() {
        return userCommission;
    }

    public void setUserCommission(int userCommission) {
        this.userCommission = userCommission;
    }

    public int getShopCommission() {
        return shopCommission;
    }

    public void setShopCommission(int shopCommission) {
        this.shopCommission = shopCommission;
    }

    public int getShipperCommission() {
        return shipperCommission;
    }

    public void setShipperCommission(int shipperCommission) {
        this.shipperCommission = shipperCommission;
    }
}
