package com.example.chodocu_ver1.data_models;

public class SanPham {
    private String iD, userID, shopID, tenSP, spImage, moTa, danhMuc, ngayDang, diaChiDang;
    private long giaTien;
    private int soLuong, tinhTrang;

    public SanPham() {
    }

    public SanPham(String iD, String userID, String shopID, String tenSP, String spImage, String moTa, String danhMuc, String ngayDang, String diaChiDang, long giaTien, int soLuong, int tinhTrang) {
        this.iD = iD;
        this.userID = userID;
        this.shopID = shopID;
        this.tenSP = tenSP;
        this.spImage = spImage;
        this.moTa = moTa;
        this.danhMuc = danhMuc;
        this.ngayDang = ngayDang;
        this.diaChiDang = diaChiDang;
        this.giaTien = giaTien;
        this.soLuong = soLuong;
        this.tinhTrang = tinhTrang;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getSpImage() {
        return spImage;
    }

    public void setSpImage(String spImage) {
        this.spImage = spImage;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }

    public String getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(String ngayDang) {
        this.ngayDang = ngayDang;
    }

    public String getDiaChiDang() {
        return diaChiDang;
    }

    public void setDiaChiDang(String diaChiDang) {
        this.diaChiDang = diaChiDang;
    }

    public long getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(long giaTien) {
        this.giaTien = giaTien;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }
}
