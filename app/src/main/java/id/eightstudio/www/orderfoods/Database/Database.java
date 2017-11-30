package id.eightstudio.www.orderfoods.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import id.eightstudio.www.orderfoods.Model.Order;

/**
 * Created by danbo on 30/11/17.
 */

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "EatitDB.db";
    private static final int DB_VER = 1;

    private static final String TABLE_ORDER = "OrderDetail";
    private static final String KEY_ID = "ProductId";
    private static final String KEY_NAME = "ProductName";
    private static final String KEY_QUANTITY = "Quantity";
    private static final String KEY_PRICE = "Price";
    private static final String KEY_DISCOUNT = "Discount";


    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductId", "ProductName", "Quantity", "Price", "Discount"};

        qb.setTables(TABLE_ORDER);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null,null);

        List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))
                        ));
            } while (c.moveToNext());
        }

        return result;
    }

    public void addToCart(Order order) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO " + TABLE_ORDER + "(ProductId, ProductName, Quantity, Price, Discount)" +
                "VALUES( '%s', '%s', '%s', '%s', '$s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount()
                );

        db.execSQL(query);
    }

    public void cleanCart() {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM " + TABLE_ORDER );
        db.execSQL(query);

    }

}
