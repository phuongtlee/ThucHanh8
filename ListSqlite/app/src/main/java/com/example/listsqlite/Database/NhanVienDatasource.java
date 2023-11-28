package com.example.listsqlite.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.listsqlite.NhanVien;

import java.util.ArrayList;
import java.util.List;

public class NhanVienDatasource {


    private SQLiteDatabase database;
    private NhanVienHelper dbHelper;
    private String[] allColumn = {NhanVienHelper.COLUMN_ID, NhanVienHelper.COLUMN_PERSON, NhanVienHelper.COLUMN_ROOM};

    public NhanVienDatasource(Context context) {
        dbHelper = new NhanVienHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public long createPerson(NhanVien nhanVien){
        ContentValues values = new ContentValues();
        values.put(NhanVienHelper.COLUMN_PERSON, nhanVien.getTennv());
        values.put(NhanVienHelper.COLUMN_ID, nhanVien.getManv());
        values.put(NhanVienHelper.COLUMN_ROOM, nhanVien.getPhongban());
        long insertId = database.insert(NhanVienHelper.TABLE_PEOPLE, null,
                values);
        return insertId;
    }

    public int deletePerson(NhanVien nhanVien){
        String selection = NhanVienHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(nhanVien.getManv())};
        int deletedRows = database.delete(NhanVienHelper.TABLE_PEOPLE, selection, selectionArgs);
        return deletedRows;
    }

    public int updatePerson(NhanVien nhanVien){
        ContentValues values = new ContentValues();
        values.put(NhanVienHelper.COLUMN_PERSON, nhanVien.getTennv());
        values.put(NhanVienHelper.COLUMN_ROOM, nhanVien.getPhongban());

        String selection = NhanVienHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(nhanVien.getManv()) };

        int count = database.update(
                NhanVienHelper.TABLE_PEOPLE,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public List<NhanVien> getAllPeople(){
        List<NhanVien> people = new ArrayList<NhanVien>();

        Cursor cursor = database.query(NhanVienHelper.TABLE_PEOPLE, allColumn, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            NhanVien person = cursorToPerson(cursor);
            people.add(person);
            cursor.moveToNext();
        }
        cursor.close();
        return people;
    }


    public List<NhanVien> searchPeople(String keyword) {
        List<NhanVien> results = new ArrayList<NhanVien>();

        Cursor cursor = database.query(NhanVienHelper.TABLE_PEOPLE, allColumn,
                NhanVienHelper.COLUMN_PERSON + " LIKE ?",
                new String[]{"%" + keyword + "%"},
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            NhanVien person = cursorToPerson(cursor);
            results.add(person);
            cursor.moveToNext();
        }
        cursor.close();
        return results;
    }

    public List<NhanVien> searchPeopleWithDepartmentRoom(String keyword) {
        List<NhanVien> results = new ArrayList<NhanVien>();

        Cursor cursor = database.query(NhanVienHelper.TABLE_PEOPLE, allColumn,
                NhanVienHelper.COLUMN_ROOM + " LIKE ?",
                new String[]{"%" + keyword + "%"},
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            NhanVien person = cursorToPerson(cursor);
            results.add(person);
            cursor.moveToNext();
        }
        cursor.close();
        return results;
    }

    private NhanVien cursorToPerson(Cursor cursor){
        NhanVien nhanVien = new NhanVien(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        return nhanVien;
    }
}
