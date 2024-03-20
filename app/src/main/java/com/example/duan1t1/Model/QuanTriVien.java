package com.example.duan1t1.Model;

public class QuanTriVien {
    private String maQTV;
    private String hoTen;
    private String matKhau;

    public QuanTriVien() {
    }

    public QuanTriVien(String maQTV, String hoTen, String matKhau) {
        this.maQTV = maQTV;
        this.hoTen = hoTen;
        this.matKhau = matKhau;
    }

    public String getMaQTV() {
        return maQTV;
    }

    public void setMaQTV(String maQTV) {
        this.maQTV = maQTV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
}
