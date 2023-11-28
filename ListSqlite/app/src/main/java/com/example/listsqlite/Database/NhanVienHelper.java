package com.example.listsqlite.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class NhanVienHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NhanVien.db";

    public static final String TABLE_PEOPLE = "nhanvien";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PERSON = "tennv";
    public static final String COLUMN_ROOM = "phongban";



    public NhanVienHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PEOPLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_PERSON + " TEXT NOT NULL," +
                COLUMN_ROOM + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(NhanVienHelper.class.getName(), "Upgrading database from version"
                + oldVersion + " to " + newVersion
                +", which will destroy all old data" );
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_PEOPLE);
    }
}
