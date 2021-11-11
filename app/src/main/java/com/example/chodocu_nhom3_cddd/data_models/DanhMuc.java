package com.example.chodocu_nhom3_cddd.data_models;

public class DanhMuc {

    private String danhMucID;
    private String tenDanhMuc;
    private String danhMucIMG;

    public DanhMuc() {
    }

    public DanhMuc(String danhMucID, String tenDanhMuc, String danhMucIMG) {
        this.danhMucID = danhMucID;
        this.tenDanhMuc = tenDanhMuc;
        this.danhMucIMG = danhMucIMG;
    }

    public String getDanhMucID() {
        return danhMucID;
    }

    public void setDanhMucID(String danhMucID) {
        this.danhMucID = danhMucID;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getDanhMucIMG() {
        return danhMucIMG;
    }

    public void setDanhMucIMG(String danhMucIMG) {
        this.danhMucIMG = danhMucIMG;
    }
}
