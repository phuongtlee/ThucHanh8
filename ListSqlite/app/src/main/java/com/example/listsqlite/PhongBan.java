package com.example.listsqlite;

public class PhongBan {

    private String tenPhong;
    private int maPhong;

    public PhongBan(){}

    public PhongBan(String tenPhong, int maPhong) {
        this.tenPhong = tenPhong;
        this.maPhong = maPhong;
    }


    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public int getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(int maPhong) {
        this.maPhong = maPhong;
    }
}
