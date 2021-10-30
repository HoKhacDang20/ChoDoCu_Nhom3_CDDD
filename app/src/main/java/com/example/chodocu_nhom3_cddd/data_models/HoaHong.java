package com.example.chodocu_nhom3_cddd.data_models;

public class HoaHong {
    private String id;
    private int userHoaHong;
    private int shopHoaHong;

    public HoaHong() {
    }

    public HoaHong(String id, int userHoaHong, int shopHoaHong) {
        this.id = id;
        this.userHoaHong = userHoaHong;
        this.shopHoaHong = shopHoaHong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserHoaHong() {
        return userHoaHong;
    }

    public void setUserHoaHong(int userHoaHong) {
        this.userHoaHong = userHoaHong;
    }

    public int getShopHoaHong() {
        return shopHoaHong;
    }

    public void setShopHoaHong(int shopHoaHong) {
        this.shopHoaHong = shopHoaHong;
    }
}
