package com.example.duan1t1.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.duan1t1.Database.DbHelper;
import com.example.duan1t1.Model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class DaoSanPham {
    private SQLiteDatabase db;
    private DbHelper dbHelper;

    public DaoSanPham(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(SanPham obj) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tenSP",obj.getTenSP());
        contentValues.put("tenHang",obj.getTenHang());
        contentValues.put("moTa",obj.getMoTa());
        contentValues.put("giaTien",obj.getGiaTien());
        contentValues.put("images", obj.getImages());

        return db.insert("SanPham",null,contentValues);
    }

    public long update(SanPham obj) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("maSP",obj.getMaSP());
        contentValues.put("tenSP",obj.getTenSP());
        contentValues.put("tenHang",obj.getTenHang());
        contentValues.put("moTa",obj.getMoTa());
        contentValues.put("giaTien",obj.getGiaTien());
        contentValues.put("images", obj.getImages());

        return db.update("SanPham",contentValues,"maSP = ?",new String[]{String.valueOf(obj.getMaSP())});
    }

    public int delete(String id) {
        return db.delete("SanPham","maSP = ?",new String[]{String.valueOf(id)});
    }

    private List<SanPham> getData(String sql, String ... selectionArgs) {
        List<SanPham> lstSach = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,selectionArgs);
        while (cursor.moveToNext()) {
            lstSach.add(new SanPham(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    Integer.parseInt(cursor.getString(4)),
                    cursor.getString(5)

            ));
        }
        return lstSach;
    }


    public SanPham selectID(int id) {
        SanPham sanPham = new SanPham();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM SanPham WHERE maSP =?", new String[]{String.valueOf(id)});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    SanPham sp = new SanPham();
                    sp.setMaSP(cursor.getInt(0));
                    sp.setTenSP(cursor.getString(1));
                    sp.setTenHang(cursor.getString(2));
                    sp.setMoTa(cursor.getString(3));
                    sp.setGiaTien(cursor.getInt(4));
                    sp.setImages(cursor.getString(5));
                    sanPham = sp;
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {

        }
//        Cursor cursor = db.rawQuery("SELECT * FROM SanPham WHERE maSP =?", new String[]{String.valueOf(id)});
//        while (cursor.moveToNext()) {
//           SanPham sp = new SanPham();
//           sp.setMaSP(cursor.getInt(0));
//           sp.setTenSP(cursor.getString(1));
//           sp.setTenHang(cursor.getString(2));
//           sp.setGiaTien(cursor.getInt(3));
//           sp.setMoTa(cursor.getString(4));
//           sanPham = sp;
//
//        }
        return sanPham;

    }
    public SanPham getID (String maSP) {
        String sql = "SELECT * FROM SanPham WHERE maSP = ?";
        List<SanPham> lstTT = getData(sql,maSP);
        return lstTT.get(0);
    }

    public List<SanPham> getAll() {
        String sql = "SELECT * FROM SanPham";
        return getData(sql);
    }

    public boolean checkID(String fieldValue) {
        String Query = "Select * from SanPham where maSP = " + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
