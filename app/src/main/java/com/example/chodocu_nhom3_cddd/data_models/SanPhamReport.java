package com.example.chodocu_nhom3_cddd.data_models;

public class SanPhamReport {
    private String reportID, userID, lyDoBaoCao;
    private SanPham sanPham;

    public SanPhamReport() {
    }

    public SanPhamReport(String reportID, String userID, String lyDoBaoCao, SanPham sanPham) {
        this.reportID = reportID;
        this.userID = userID;
        this.lyDoBaoCao = lyDoBaoCao;
        this.sanPham = sanPham;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLyDoBaoCao() {
        return lyDoBaoCao;
    }

    public void setLyDoBaoCao(String lyDoBaoCao) {
        this.lyDoBaoCao = lyDoBaoCao;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }
}
