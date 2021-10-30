package com.example.chodocu_nhom3_cddd.data_models;

public class CuocHen {
    private String idCuocHen;
    private String tieuDe;
    private String ngayHen;
    private String moTaCuocHen;
    private String nguoiHenID;
    private String nguoiDuocHenID;
    private boolean active;

    public CuocHen(){}
    public CuocHen(String idCuocHen, String tieuDe, String ngayHen, String moTaCuocHen, String nguoiHenID, String nguoiDuocHenID, boolean active) {
        this.idCuocHen = idCuocHen;
        this.tieuDe = tieuDe;
        this.ngayHen = ngayHen;
        this.moTaCuocHen = moTaCuocHen;
        this.nguoiHenID = nguoiHenID;
        this.nguoiDuocHenID = nguoiDuocHenID;
        this.active = active;
    }

    public String getIdCuocHen() {
        return idCuocHen;
    }

    public void setIdCuocHen(String idCuocHen) {
        this.idCuocHen = idCuocHen;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNgayHen() {
        return ngayHen;
    }

    public void setNgayHen(String ngayHen) {
        this.ngayHen = ngayHen;
    }

    public String getMoTaCuocHen() {
        return moTaCuocHen;
    }

    public void setMoTaCuocHen(String moTaCuocHen) {
        this.moTaCuocHen = moTaCuocHen;
    }

    public String getNguoiHenID() {
        return nguoiHenID;
    }

    public void setNguoiHenID(String nguoiHenID) {
        this.nguoiHenID = nguoiHenID;
    }

    public String getNguoiDuocHenID() {
        return nguoiDuocHenID;
    }

    public void setNguoiDuocHenID(String nguoiDuocHenID) {
        this.nguoiDuocHenID = nguoiDuocHenID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
