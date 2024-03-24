package com.example.duan1t1.Model;

public class SanPham {
    private int maSP;

    private String tenSP;
    private String tenHang;
    private String moTa;
    private int giaTien;

    private String images;

    public SanPham() {
    }

    public SanPham(int maSP, String tenSP, String tenHang, String moTa, int giaTien, String images) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.tenHang = tenHang;
        this.moTa = moTa;
        this.giaTien = giaTien;
        this.images = images;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public int getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(int giaTien) {
        this.giaTien = giaTien;
    }

}
