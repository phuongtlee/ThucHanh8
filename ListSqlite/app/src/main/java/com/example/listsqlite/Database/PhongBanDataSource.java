package com.example.listsqlite.Database;

import static com.example.listsqlite.Database.PhongBanHelper.TBL_PHONG_MAPH;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.listsqlite.PhongBan;

import java.util.ArrayList;
import java.util.List;

public class PhongBanDataSource {

    PhongBanHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    public PhongBanDataSource(Context context) {
        this.context = context;
        this.dbHelper = new PhongBanHelper(context);
    }

    public PhongBanDataSource open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public List<PhongBan> danhsachPhong() {
        List<PhongBan> list = new ArrayList<PhongBan>();
        String sql = "select * from " + PhongBanHelper.TBL_PHONG;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PhongBan ph = new PhongBan(cursor.getString(1), cursor.getInt(0));
            list.add(ph);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    @SuppressLint("Range")
    public PhongBan timPhongtheoMa(int maPH) {
        PhongBan temp = new PhongBan();
        String sql = "select * from " + PhongBanHelper.TBL_PHONG + " where " + PhongBanHelper.TBL_PHONG_MAPH + " = " + maPH;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            temp.setMaPhong(maPH);
            temp.setTenPhong(cursor.getString(cursor.getColumnIndex(PhongBanHelper.TBL_PHONG_TENPH)));
            cursor.moveToNext();
        }
        cursor.close();
        return temp;
    }


    public String themPhong(PhongBan ph) {
        ContentValues values = new ContentValues();
        values.put(PhongBanHelper.TBL_PHONG_TENPH, ph.getTenPhong());

        long ma = db.insert(PhongBanHelper.TBL_PHONG, null, values);
        String tenph = timPhongtheoMa((int) ma).getTenPhong();
        return tenph;
    }

    public int soluongphong() {
        String sql = "select count(*) as soluong from " + PhongBanHelper.TBL_PHONG;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int soluong = 0;
        while (!cursor.isAfterLast()) {
            soluong = cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();
        return soluong;
    }

    public int xoaPhongtheoMa(int maPH) {
        String whereClause =PhongBanHelper.TBL_PHONG_MAPH + "=?";
        String[] whereArgs = new String[]{String.valueOf(maPH)};
        return (db.delete(PhongBanHelper.TBL_PHONG, whereClause, whereArgs));
    }

    public void capnhatPhong(int maPH, String ten, String mota) {
        ContentValues values = new ContentValues();
        values.put(PhongBanHelper.TBL_PHONG_TENPH, ten);

        String whereClause = PhongBanHelper.TBL_PHONG_MAPH + "=?";
        String[] whereArgs = new String[]{String.valueOf(maPH)};
        db.update(PhongBanHelper.TBL_PHONG, values, whereClause, whereArgs);
    }

}
