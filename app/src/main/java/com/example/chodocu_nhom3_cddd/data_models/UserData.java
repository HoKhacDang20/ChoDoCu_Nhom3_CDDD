package com.example.chodocu_nhom3_cddd.data_models;

public class UserData {
    private String userName, shopID, hoTen, soDienThoai, gioiTinh, diaChi, password, image, userID, ngayThamGia, soCMND, email;
    private int permission, hoaHong, tinhTrang, soSPDaBan, diemThanhVien, report;
    private long money;

    public UserData(String userName, String shopID, String hoTen, String soDienThoai, String gioiTinh, String diaChi, String password, String image, String userID, String ngayThamGia, String soCMND, String email, int permission, int hoaHong, int tinhTrang, int soSPDaBan, int diemThanhVien, int report, long money) {
        this.userName = userName;
        this.shopID = shopID;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.password = password;
        this.image = image;
        this.userID = userID;
        this.ngayThamGia = ngayThamGia;
        this.soCMND = soCMND;
        this.email = email;
        this.permission = permission;
        this.hoaHong = hoaHong;
        this.tinhTrang = tinhTrang;
        this.soSPDaBan = soSPDaBan;
        this.diemThanhVien = diemThanhVien;
        this.report = report;
        this.money = money;
    }

    public UserData() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNgayThamGia() {
        return ngayThamGia;
    }

    public void setNgayThamGia(String ngayThamGia) {
        this.ngayThamGia = ngayThamGia;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public int getHoaHong() {
        return hoaHong;
    }

    public void setHoaHong(int hoaHong) {
        this.hoaHong = hoaHong;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public int getSoSPDaBan() {
        return soSPDaBan;
    }

    public void setSoSPDaBan(int soSPDaBan) {
        this.soSPDaBan = soSPDaBan;
    }

    public int getDiemThanhVien() {
        return diemThanhVien;
    }

    public void setDiemThanhVien(int diemThanhVien) {
        this.diemThanhVien = diemThanhVien;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public String getSoCMND() {
        return soCMND;
    }

    public void setSoCMND(String soCMND) {
        this.soCMND = soCMND;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
