package com.example.duan1t1.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.duan1t1.Database.DbHelper;
import com.example.duan1t1.Model.QuanTriVien;

import java.util.ArrayList;
import java.util.List;

public class DAOQuanTriVien {

    private SQLiteDatabase db;

    public DAOQuanTriVien(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(QuanTriVien obj) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("maQTV",obj.getMaQTV());
        contentValues.put("hoTen",obj.getHoTen());
        contentValues.put("matKhau",obj.getMatKhau());

        return db.insert("QuanTriVien",null,contentValues);
    }

    public long update(QuanTriVien obj) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("maQTV",obj.getMaQTV());
        contentValues.put("hoTen",obj.getHoTen());
        contentValues.put("matKhau",obj.getMatKhau());

        return db.update("QuanTriVien",contentValues,"maQTV = ?",new String[]{String.valueOf(obj.getMaQTV())});
    }

    public int delete(String id) {
        return db.delete("QuanTriVien","maQTV = ?",new String[]{String.valueOf(id)});
    }

    private List<QuanTriVien> getData(String sql, String ... selectionArgs) {
        List<QuanTriVien> lstTT = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,selectionArgs);
        while (cursor.moveToNext()) {
            lstTT.add(new QuanTriVien(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2)

            ));
        }
        return lstTT;
    }

    public QuanTriVien getID(String id) {
        String sql = "SELECT * FROM QuanTriVien WHERE maQTV = ?";
        List<QuanTriVien> lstTT = getData(sql,id);
        return lstTT.get(0);
    }

    public List<QuanTriVien> getAll() {
        String sql = "SELECT * FROM QUANTRIVIEN";
        return getData(sql);
    }

    public long checkLogin(String username,String password) {
        Cursor cursor = db.rawQuery("SELECT * FROM QuanTriVien WHERE maQTV = ? AND matKhau = ?",new String[]{username,password});
        if (cursor.getCount() > 0) {
            return 1;
        } else {
            return -1;
        }
    }

}
