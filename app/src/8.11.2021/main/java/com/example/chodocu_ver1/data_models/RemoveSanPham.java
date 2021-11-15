package com.example.chodocu_ver1.data_models;

public class RemoveSanPham {
    private String id;
    private SanPham sanPham;

    public RemoveSanPham() {
    }

    public RemoveSanPham(String id, SanPham sanPham) {
        this.id = id;
        this.sanPham = sanPham;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }
}
