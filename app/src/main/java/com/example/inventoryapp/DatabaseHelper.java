package com.example.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "inventory.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE products (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, stock INTEGER, category TEXT)");
        db.execSQL("CREATE TABLE sales (id INTEGER PRIMARY KEY AUTOINCREMENT, productName TEXT, quantity INTEGER, total REAL, date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS sales");
        onCreate(db);
    }

    public boolean addProduct(String name, double price, int stock, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("stock", stock);
        values.put("category", category);
        long result = db.insert("products", null, values);
        return result != -1;
    }

    public ArrayList<Product> getProducts() {
        ArrayList<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Product(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getInt(3),
                    cursor.getString(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean updateProduct(int id, String name, double price, int stock, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("stock", stock);
        values.put("category", category);
        return db.update("products", values, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("products", "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public ArrayList<Product> getLowStock() {
        ArrayList<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE stock < 5", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Product(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getInt(3),
                    cursor.getString(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean recordSale(int productId, String productName, double price, int qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Update stock
        Product p = getProducts().stream()
            .filter(prod -> prod.id == productId)
            .findFirst()
            .orElse(null);
        
        if (p != null) {
            ContentValues pv = new ContentValues();
            pv.put("stock", p.stock - qty);
            db.update("products", pv, "id=?", new String[]{String.valueOf(productId)});
        }
        
        // Record sale
        ContentValues sv = new ContentValues();
        sv.put("productName", productName);
        sv.put("quantity", qty);
        sv.put("total", price * qty);
        sv.put("date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        
        return db.insert("sales", null, sv) != -1;
    }

    public ArrayList<Sale> getSales() {
        ArrayList<Sale> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM sales ORDER BY date DESC", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Sale(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getDouble(3),
                    cursor.getString(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public double getTotalSales() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(total) FROM sales", null);
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    public double getInventoryValue() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(price * stock) FROM products", null);
        double value = 0;
        if (cursor.moveToFirst()) {
            value = cursor.getDouble(0);
        }
        cursor.close();
        return value;
    }
}
