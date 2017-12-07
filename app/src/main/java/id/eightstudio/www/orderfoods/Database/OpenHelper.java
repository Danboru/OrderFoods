package id.eightstudio.www.orderfoods.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import id.eightstudio.www.orderfoods.Model.Order;
import id.eightstudio.www.orderfoods.Model.User;

public class OpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "OpenHelper";
    
    private static final String DB_NAME = "Order.db";
    private static final int DB_VER = 1;

    private static final String TABLE_ORDER = "OrderDetail";
    private static final String KEY_ID = "ProductId";
    private static final String KEY_NAME = "ProductName";
    private static final String KEY_QUANTITY = "Quantity";
    private static final String KEY_PRICE = "Price";
    private static final String KEY_DISCOUNT = "Discount";
    private static final String KEY_USER = "UserId";

    private static final String TABLE_CURRENT_USER = "CurrentUser";
    private static final String KEY_PHONE = "UserName";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_IS_STAFF = "IsStaff";

    SQLiteDatabase sqliteDatabase;

    public OpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDER + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
                + KEY_QUANTITY + " TEXT, " + KEY_PRICE + " TEXT, "
                + KEY_DISCOUNT + " TEXT, " + KEY_USER + " TEXT" + ")";

        String CREATE_CURRENT_USER_TABLE = "CREATE TABLE " + TABLE_CURRENT_USER + "("
                + KEY_PHONE + " STRING PRIMARY KEY, " + KEY_PASSWORD + " TEXT, "
                + KEY_IS_STAFF + " TEXT " + ")";

        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_CURRENT_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT_USER);
    }

    //FIX
    public void addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, order.getProductName());
        values.put(KEY_QUANTITY, order.getQuantity());
        values.put(KEY_PRICE, order.getPrice());
        values.put(KEY_DISCOUNT, order.getDiscount());
        values.put(KEY_USER, order.getKeyUser());

        // Inserting Row
        db.insert(TABLE_ORDER, null, values);
    }

    //FIX
    public ArrayList<Order> getAllOrder() {
        ArrayList<Order> orderList = new ArrayList<Order>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ORDER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping semua data dan menginputkan data ke dalamnya
        try {

            if (cursor.moveToFirst()) {
                do {
                    Order order = new Order();
                    order.setProductId(cursor.getString(0));
                    order.setProductName(cursor.getString(1));
                    order.setQuantity(cursor.getString(2));
                    order.setPrice(cursor.getString(3));
                    order.setDiscount(cursor.getString(4));

                    //Menambahkan user ke list
                    orderList.add(order);

                } while (cursor.moveToNext());
            }

        } finally {
            Log.d(TAG, "CursorWindowAllocationException");
        }

        // return orderList
        return orderList;
    }

    public ArrayList<Order> getAllOrderFilter(String userId) {
        ArrayList<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        // Select All Query
        String selectString = "SELECT * FROM " + TABLE_ORDER + " WHERE " + KEY_USER + " =?";

        Log.d(TAG, "Menjalankan Query : " + selectString);
        Cursor cursor = db.rawQuery(selectString, new String[] {userId});

        //Hitung Jumlah data
        int count = 0;
            while(cursor.moveToNext()){
                count++;
            }

            //here, count is records found
            Log.d(TAG, String.format("%d Records Found", count));

        // Looping semua data dan menginputkan data ke dalamnya
        try {

            if (cursor.moveToFirst()) {
                do {
                    Order order = new Order();
                    order.setProductId(cursor.getString(0));
                    order.setProductName(cursor.getString(1));
                    order.setQuantity(cursor.getString(2));
                    order.setPrice(cursor.getString(3));
                    order.setDiscount(cursor.getString(4));

                    //Menambahkan order ke list
                    orderList.add(order);

                } while (cursor.moveToNext());
            }

        } finally {
            Log.d(TAG, "CursorWindowAllocationException");
        }

        // return orderList
        return orderList;
    }

    //FIX
    public int updateOrder(Order kosumen, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, kosumen.getQuantity());

        // updating
        return db.update(TABLE_ORDER, values, KEY_NAME + " = ?",
                new String[] { String.valueOf(name) });
    }

    //FIX
    public void deleteOrder(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDER, KEY_NAME + " = ?",
                new String[] { String.valueOf(name) });

    }

    //FIX
    public void deleteAllOrder(SQLiteDatabase db){
        db.execSQL("delete from "+ TABLE_ORDER);
    }

    //FIX
    //Taken from https://stackoverflow.com/questions/20415309/android-sqlite-how-to-check-if-a-record-exists
    public boolean hasObject(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_ORDER + " WHERE " + KEY_NAME + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {name});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }

            //here, count is records found
            Log.d(TAG, String.format("%d records found", count));

            //endregion
        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }

    //FIX
    public int getOrderCount() {
        String countQuery = "SELECT * FROM " + TABLE_ORDER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();

        return cnt;
    }

    //
    public int getOrderCountFilter(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_ORDER + " WHERE " + KEY_USER + " =?";

        Cursor cursor = db.rawQuery(countQuery, new String[] {userId});
        int cnt = cursor.getCount();

        return cnt;
    }

    //
    public boolean isTableExists(String tableName, boolean openDb) {

        if(openDb) {
            if(sqliteDatabase == null || !sqliteDatabase.isOpen()) {
                sqliteDatabase = getReadableDatabase();
            }

            if(!sqliteDatabase.isReadOnly()) {
                sqliteDatabase.close();
                sqliteDatabase = getReadableDatabase();
            }
        }

        Cursor cursor = sqliteDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+ tableName +"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

}
