package com.test.databarang;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Objects;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Produk";
    private static final String TABLE_BARANG = "barang";
    private static final String KEY_KODE = "kode";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_BELI = "beli";
    private static final String KEY_JUAL = "jual";
    private static final String KEY_STOK = "stok";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_TABLE_BARANG = "CREATE TABLE " +
                TABLE_BARANG + "("
                + KEY_KODE + " TEXT PRIMARY KEY,"
                + KEY_NAMA + " TEXT,"
                + KEY_BELI + " INTEGER,"
                + KEY_JUAL + " INTEGER,"
                + KEY_STOK + " INTEGER)";
        db.execSQL(query_TABLE_BARANG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BARANG);
        onCreate(db);
    }

    public void addBarang(String kode, String nama, int beli, int jual, int stok) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KODE, kode);
        values.put(KEY_NAMA, nama);
        values.put(KEY_BELI, beli);
        values.put(KEY_JUAL, jual);
        values.put(KEY_STOK, stok);
        db.insert(TABLE_BARANG, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public Barang readBarang(String kode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorBarang = db.rawQuery("SELECT * FROM " + TABLE_BARANG + " WHERE " + KEY_KODE + " = ?", new String[]{kode});
        cursorBarang.moveToFirst();
        Barang barang = new Barang(
                cursorBarang.getString(cursorBarang.getColumnIndex(KEY_KODE)),
                cursorBarang.getString(cursorBarang.getColumnIndex(KEY_NAMA)),
                cursorBarang.getInt(cursorBarang.getColumnIndex(KEY_BELI)),
                cursorBarang.getInt(cursorBarang.getColumnIndex(KEY_JUAL)),
                cursorBarang.getInt(cursorBarang.getColumnIndex(KEY_STOK)));
        return barang;
    }

    @SuppressLint("Range")
    public ArrayList<Barang> readAllBarang() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorBarang = db.rawQuery("SELECT * FROM " + TABLE_BARANG, null);
        ArrayList<Barang> courseModalArrayList = new ArrayList<>();

        if (cursorBarang.moveToFirst()) {
            do {
                courseModalArrayList.add(new Barang(
                        cursorBarang.getString(cursorBarang.getColumnIndex(KEY_KODE)),
                        cursorBarang.getString(cursorBarang.getColumnIndex(KEY_NAMA)),
                        cursorBarang.getInt(cursorBarang.getColumnIndex(KEY_BELI)),
                        cursorBarang.getInt(cursorBarang.getColumnIndex(KEY_JUAL)),
                        cursorBarang.getInt(cursorBarang.getColumnIndex(KEY_STOK))));
            } while (cursorBarang.moveToNext());
        }
        cursorBarang.close();
        return courseModalArrayList;
    }

    public boolean checkForTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BARANG, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count > 0) {
                return true;
            }
            cursor.close();
        }
        return false;
    }

    @SuppressLint({"Range", "DefaultLocale"})
    public String readKode() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorBarang = db.rawQuery("SELECT * FROM " + TABLE_BARANG, null);
        ArrayList<Integer> kodeList = new ArrayList<>();
        ArrayList<Integer> iterate = new ArrayList<>();

        if (!checkForTables()) return String.format("B%03d", 1);
        if (cursorBarang.moveToFirst()) {
            do {
                kodeList.add(Integer.parseInt(cursorBarang.getString(cursorBarang.getColumnIndex(KEY_KODE)).replaceAll("[^0-9]", "")));
            } while (cursorBarang.moveToNext());
        }

        cursorBarang.close();
        for (int i = 1; i <= getMax(kodeList); i++) {
            if (!kodeList.contains(i)) return String.format("B%03d", i);
            else iterate.add(i);
        }
        return String.format("B%03d", getMax(kodeList) + 1);
    }

    @SuppressLint("Range")
    public boolean checkKode(String kode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorBarang = db.rawQuery("SELECT * FROM " + TABLE_BARANG, null);

        if (cursorBarang.moveToFirst()) {
            do {
                if (Objects.equals(cursorBarang.getString(cursorBarang.getColumnIndex(KEY_KODE)), kode)) return true;
            } while (cursorBarang.moveToNext());
        }
        return false;
    }

    public int getMax(ArrayList<Integer> list) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) > max) {
                max = list.get(i);
            }
        }
        return max;
    }

    public void deleteRow(String kb) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BARANG, KEY_KODE + "='" + kb + "'", null);
        db.close();
    }

    public void updateMethod(String kode, String nama, int beli, int jual, int stok) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update " + TABLE_BARANG + " set nama='" + nama + "', beli = '" + beli + "', jual = '" + jual + "', stok = '" + stok + "' where kode='" + kode + "'");
        db.close();
    }
}