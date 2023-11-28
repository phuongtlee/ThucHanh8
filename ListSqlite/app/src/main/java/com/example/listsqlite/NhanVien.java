package com.example.listsqlite;

import java.io.Serializable;

public class NhanVien implements Serializable {
    int manv;
    String tennv;
    String phongban;

    public NhanVien(int id, String tennv, String phongban) {
        this.tennv = tennv;
        this.manv = id;
        this.phongban = phongban;
    }


    public int getManv() {
        return manv;
    }

    public String getTennv() {
        return tennv;
    }

    public void setManv(int manv) {
        this.manv = manv;
    }

    public void setTennv(String tennv) {
        this.tennv = tennv;
    }

    public String getPhongban(){
        return phongban;
    }

    public void setPhongban(String phongban){
        this.phongban = phongban;
    }

    @Override
    public String toString() {
        return new String(this.manv + "-" + this.tennv + "-" + this.phongban);
    }
}
