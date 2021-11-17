package com.example.chodocu_nhom3_cddd.data_models;

public class DanhGia {
    private String cmtID, sanPhamID, userID, moTaComment, ngayComment;
    private int soSao;

    public DanhGia(){}

    public DanhGia(String cmtID, String sanPhamID, String userID, String moTaComment, String ngayComment, int soSao) {
        this.cmtID = cmtID;
        this.sanPhamID = sanPhamID;
        this.userID = userID;
        this.moTaComment = moTaComment;
        this.ngayComment = ngayComment;
        this.soSao = soSao;
    }

    public String getCmtID() {
        return cmtID;
    }

    public void setCmtID(String cmtID) {
        this.cmtID = cmtID;
    }

    public String getSanPhamID() {
        return sanPhamID;
    }

    public void setSanPhamID(String sanPhamID) {
        this.sanPhamID = sanPhamID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMoTaComment() {
        return moTaComment;
    }

    public void setMoTaComment(String moTaComment) {
        this.moTaComment = moTaComment;
    }

    public String getNgayComment() {
        return ngayComment;
    }

    public void setNgayComment(String ngayComment) {
        this.ngayComment = ngayComment;
    }

    public int getSoSao() {
        return soSao;
    }

    public void setSoSao(int soSao) {
        this.soSao = soSao;
    }
}
