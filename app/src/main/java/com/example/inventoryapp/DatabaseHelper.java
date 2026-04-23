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
    private static final String DB = "inventory.db";
    private static final int VER = 1;

    public DatabaseHelper(Context c) { super(c, DB, null, VER); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE products(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, stock INTEGER, category TEXT)");
        db.execSQL("CREATE TABLE sales(id INTEGER PRIMARY KEY AUTOINCREMENT, productName TEXT, quantity INTEGER, total REAL, date TEXT)");
    }
    @Override public void onUpgrade(SQLiteDatabase db, int o, int n) { db.execSQL("DROP TABLE IF EXISTS products"); db.execSQL("DROP TABLE IF EXISTS sales"); onCreate(db); }

    public boolean addProduct(String name, double price, int stock, String category) {
        ContentValues v = new ContentValues();
        v.put("name", name); v.put("price", price); v.put("stock", stock); v.put("category", category);
        return db().insert("products", null, v) != -1;
    }
    public ArrayList<Product> getProducts() {
        ArrayList<Product> list = new ArrayList<>();
        Cursor c = db().rawQuery("SELECT * FROM products", null);
        if(c.moveToFirst()) do { list.add(new Product(c.getInt(0), c.getString(1), c.getDouble(2), c.getInt(3), c.getString(4))); } while(c.moveToNext());
        return list;
    }
    public boolean updateProduct(int id, String name, double price, int stock, String category) {
        ContentValues v = new ContentValues();
        v.put("name", name); v.put("price", price); v.put("stock", stock); v.put("category", category);
        return db().update("products", v, "id=?", new String[]{String.valueOf(id)}) > 0;
    }
    public boolean deleteProduct(int id) { return db().delete("products", "id=?", new String[]{String.valueOf(id)}) > 0; }
    public ArrayList<Product> getLowStock() {
        ArrayList<Product> list = new ArrayList<>();
        Cursor c = db().rawQuery("SELECT * FROM products WHERE stock < 5", null);
        if(c.moveToFirst()) do { list.add(new Product(c.getInt(0), c.getString(1), c.getDouble(2), c.getInt(3), c.getString(4))); } while(c.moveToNext());
        return list;
    }
    public boolean recordSale(int productId, String productName, double price, int qty) {
        ContentValues pv = new ContentValues(); pv.put("stock", "stock - " + qty);
        db().update("products", pv, "id=?", new String[]{String.valueOf(productId)});
        ContentValues sv = new ContentValues();
        sv.put("productName", productName); sv.put("quantity", qty); sv.put("total", price * qty);
        sv.put("date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        return db().insert("sales", null, sv) != -1;
    }
    public ArrayList<Sale> getSales() {
        ArrayList<Sale> list = new ArrayList<>();
        Cursor c = db().rawQuery("SELECT * FROM sales ORDER BY date DESC", null);
        if(c.moveToFirst()) do { list.add(new Sale(c.getInt(0), c.getString(1), c.getInt(2), c.getDouble(3), c.getString(4))); } while(c.moveToNext());
        return list;
    }
    public double getTotalSales() {
        Cursor c = db().rawQuery("SELECT SUM(total) FROM sales", null);
        if(c.moveToFirst()) return c.getDouble(0); return 0;
    }
    public double getInventoryValue() {
        Cursor c = db().rawQuery("SELECT SUM(price * stock) FROM products", null);
        if(c.moveToFirst()) return c.getDouble(0); return 0;
    }
    private SQLiteDatabase db() { return getWritableDatabase(); }
}
