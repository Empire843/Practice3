package com.example.practice3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.practice3.model.Item;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ThucHanh3.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table items(" + "id integer primary key autoincrement, " + "title text, " + "category text, " + "price text, " + "date text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public void addItem(Item item) {
        String sql = "insert into items(title, category, price, date) values(?, ?, ?, ?)";
        String[] args = {item.getTitle(), item.getCategory(), item.getPrice(), item.getDate()};
        SQLiteDatabase st = getWritableDatabase();

        st.execSQL(sql, args);
        st.close();
    }

    //    public Long addItem(Item item) {
//        ContentValues values = new ContentValues();
//        values.put("title", item.getTitle());
//        values.put("category", item.getCategory());
//        values.put("price", item.getPrice());
//        values.put("date", item.getDate());
//        SQLiteDatabase st = getWritableDatabase();
//        return st.insert("items", null, values);
//    }
    public int update(Item item) {
        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("category", item.getCategory());
        values.put("price", item.getPrice());
        values.put("date", item.getDate());
        SQLiteDatabase st = getWritableDatabase();
        return st.update("items", values, "id = ?", new String[]{String.valueOf(item.getId())});
    }

    public int delete(int id) {
        SQLiteDatabase st = getWritableDatabase();
        return st.delete("items", "id = ?", new String[]{String.valueOf(id)});
    }

    //function get all order by date descending
    public List<Item> getAll() {
        List<Item> list = new ArrayList<>();
        String sql = "select * from items order by date desc";
        SQLiteDatabase st = getReadableDatabase();
        Cursor cursor = st.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String category = cursor.getString(2);
                String price = cursor.getString(3);
                String date = cursor.getString(4);
                Item item = new Item(id, title, category, price, date);
                list.add(item);
            } while (cursor.moveToNext());
        }
        return list;
    }

    // get All Item by Date current
    public List<Item> getByDate(String date) {
        List<Item> list = new ArrayList<>();
        String sql = "select * from items where date = ? order by date desc";
        String[] args = {date};
        SQLiteDatabase st = getReadableDatabase();
        Cursor cursor = st.rawQuery(sql, args);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String category = cursor.getString(2);
                String price = cursor.getString(3);
                String date1 = cursor.getString(4);
                Item item = new Item(id, title, category, price, date1);
                list.add(item);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<Item> searchByTitle(String key) {
        List<Item> list = new ArrayList<>();
        String sql = "select * from items where title like ?";
        String[] args = {"%" + key + "%"};
        SQLiteDatabase st = getReadableDatabase();
        Cursor cursor = st.rawQuery(sql, args);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String category = cursor.getString(2);
                String price = cursor.getString(3);
                String date1 = cursor.getString(4);
                Item item = new Item(id, title, category, price, date1);
                list.add(item);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<Item> searchByCategory(String category) {
        List<Item> list = new ArrayList<>();
        String sql = "select * from items where category like ?";
        String[] args = {category};
        SQLiteDatabase st = getReadableDatabase();
        Cursor cursor = st.rawQuery(sql, args);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String category1 = cursor.getString(2);
                String price = cursor.getString(3);
                String date1 = cursor.getString(4);
                Item item = new Item(id, title, category1, price, date1);
                list.add(item);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<Item> searchByDateFromTo(String from, String to, String category) {
        List<Item> list = new ArrayList<>();
        String whereClause = "date BETWEEN ? AND ?";
        String[] whereArgs = {from.trim(), to.trim()};
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("items", null, whereClause, whereArgs, null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String t = rs.getString(1);
            String c = rs.getString(2);
            String p = rs.getString(3);
            String d = rs.getString(4);
            list.add(new Item(id, t, c, p, d));
        }
        return list;
    }
}


