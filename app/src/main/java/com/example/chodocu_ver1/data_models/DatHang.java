package com.example.chodocu_ver1.data_models;

public class DatHang {
    private String donHangID, nguoiMuaID, nguoiBanID, ngayTaoDonHang, soDienThoai, diaChi;
    private SanPham sanPham;
    private int loaiDonHang, tinhTrang, sellerCommission;
    private long giaTien;
    private String shipperID;

    public DatHang() {
    }

    public DatHang(String donHangID, String nguoiMuaID, String nguoiBanID, String ngayTaoDonHang, String soDienThoai, String diaChi, SanPham sanPham, int loaiDonHang, int tinhTrang, int sellerCommission, long giaTien, String shipperID) {
        this.donHangID = donHangID;
        this.nguoiMuaID = nguoiMuaID;
        this.nguoiBanID = nguoiBanID;
        this.ngayTaoDonHang = ngayTaoDonHang;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.sanPham = sanPham;
        this.loaiDonHang = loaiDonHang;
        this.tinhTrang = tinhTrang;
        this.sellerCommission = sellerCommission;
        this.giaTien = giaTien;
        this.shipperID = shipperID;
    }

    public String getDonHangID() {
        return donHangID;
    }

    public void setDonHangID(String donHangID) {
        this.donHangID = donHangID;
    }

    public String getNguoiMuaID() {
        return nguoiMuaID;
    }

    public void setNguoiMuaID(String nguoiMuaID) {
        this.nguoiMuaID = nguoiMuaID;
    }

    public String getNguoiBanID() {
        return nguoiBanID;
    }

    public void setNguoiBanID(String nguoiBanID) {
        this.nguoiBanID = nguoiBanID;
    }

    public String getNgayTaoDonHang() {
        return ngayTaoDonHang;
    }

    public void setNgayTaoDonHang(String ngayTaoDonHang) {
        this.ngayTaoDonHang = ngayTaoDonHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public int getLoaiDonHang() {
        return loaiDonHang;
    }

    public void setLoaiDonHang(int loaiDonHang) {
        this.loaiDonHang = loaiDonHang;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public int getSellerCommission() {
        return sellerCommission;
    }

    public void setSellerCommission(int sellerCommission) {
        this.sellerCommission = sellerCommission;
    }

    public long getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(long giaTien) {
        this.giaTien = giaTien;
    }

    public String getShipperID() {
        return shipperID;
    }

    public void setShipperID(String shipperID) {
        this.shipperID = shipperID;
    }
}
