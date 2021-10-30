package com.example.chodocu_nhom3_cddd.data_models;

public class UserReport {
    private String reportID, doiTuongReportID, userTaoReportID, moTaReport;
    private int loaiReport;
    private boolean tinhTrangXem;

    public UserReport() {
    }

    public UserReport(String reportID, String doiTuongReportID, String userTaoReportID, String moTaReport, int loaiReport, boolean tinhTrangXem) {
        this.reportID = reportID;
        this.doiTuongReportID = doiTuongReportID;
        this.userTaoReportID = userTaoReportID;
        this.moTaReport = moTaReport;
        this.loaiReport = loaiReport;
        this.tinhTrangXem = tinhTrangXem;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getDoiTuongReportID() {
        return doiTuongReportID;
    }

    public void setDoiTuongReportID(String doiTuongReportID) {
        this.doiTuongReportID = doiTuongReportID;
    }

    public String getUserTaoReportID() {
        return userTaoReportID;
    }

    public void setUserTaoReportID(String userTaoReportID) {
        this.userTaoReportID = userTaoReportID;
    }

    public String getMoTaReport() {
        return moTaReport;
    }

    public void setMoTaReport(String moTaReport) {
        this.moTaReport = moTaReport;
    }

    public int getLoaiReport() {
        return loaiReport;
    }

    public void setLoaiReport(int loaiReport) {
        this.loaiReport = loaiReport;
    }

    public boolean isTinhTrangXem() {
        return tinhTrangXem;
    }

    public void setTinhTrangXem(boolean tinhTrangXem) {
        this.tinhTrangXem = tinhTrangXem;
    }

//    public boolean getObjectReportID() {
//    }
}
