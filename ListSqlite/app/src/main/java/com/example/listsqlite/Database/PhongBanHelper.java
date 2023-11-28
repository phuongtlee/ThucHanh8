package com.example.listsqlite.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PhongBanHelper extends SQLiteOpenHelper {

    List<String> create_sql_list = new ArrayList<String>();
    String sql;

    private static final String DATABASE_NAME = "PhongBan.db";
    private static final int DATABASE_VERSION = 6;

    public static final String TBL_PHONG = "tblPhongBan";
    public static final String TBL_PHONG_MAPH = "MAPH";
    public static final String TBL_PHONG_TENPH = "TENPH";

    public PhongBanHelper (@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void create_table_PhongBan() {
        sql = "CREATE TABLE " + TBL_PHONG + " (" +
                TBL_PHONG_MAPH + " INTEGER PRIMARY KEY, " +
                TBL_PHONG_TENPH+ " TEXT NOT NULL);";
        create_sql_list.add(sql);

        sql = "INSERT INTO " + TBL_PHONG + " VALUES(1,\"Moi chon phong ban\")";
        create_sql_list.add(sql);
        sql = "INSERT INTO " + TBL_PHONG + " VALUES(2,\"Phong Hanh chinh\")";
        create_sql_list.add(sql);
        sql = "INSERT INTO " + TBL_PHONG + " VALUES(3,\"Phong Ban Hang\")";
        create_sql_list.add(sql);
        sql = "INSERT INTO " + TBL_PHONG + " VALUES(4,\"Phong ke toan\")";
        create_sql_list.add(sql);
        sql = "INSERT INTO " + TBL_PHONG + " VALUES(5,\"Phong kho\")";
        create_sql_list.add(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TBL_PHONG);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        create_table_PhongBan();
        for(int i = 0;i < create_sql_list.size();i++)
            db.execSQL(create_sql_list.get(i));
    }
}
