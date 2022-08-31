package moun.com.deli.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import moun.com.deli.model.Orders;

/**
 * This Class using SQLiteDatabase object provides methods for SQLite CRUD
 * (Create, Read, Update, Delete) operations.
 */
public class OrdersDAO extends ItemsDBDAO {

    private static final String WHERE_ID_EQUALS = DataBaseHelper.ORDERED + " =?";

    public OrdersDAO(Context context) {
        super(context);
    }

    public long saveOrder(Orders orders) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.ORDERED, orders.getOrdered()== true ? 1:0);
        values.put(DataBaseHelper.CREATED_AT, System.currentTimeMillis());

        return database.insert(DataBaseHelper.ORDERS_TABLE, null, values);
    }

    public ArrayList<Orders> getOrders() {
        ArrayList<Orders> orderItems = new ArrayList<Orders>();

        String sql = "SELECT * FROM " + DataBaseHelper.ORDERS_TABLE
                + " WHERE " + DataBaseHelper.ORDERED + " = ?";
        Cursor cursor = database.rawQuery(sql, new String[] { 1 + "" });

        while (cursor.moveToNext()) {
            Orders orders = new Orders();
            orders.setId(cursor.getInt(0));
            orders.setOrdered(cursor.getInt(1)>0);
            orders.setDate_created(cursor.getLong(2));
            orderItems.add(orders);
        }
        return orderItems;
    }

    //Retrieves a single order record with the item name
    public Orders getOrder(int ordered) {
        Orders orders = null;

        String sql = "SELECT * FROM " + DataBaseHelper.ORDERS_TABLE
                + " WHERE " + DataBaseHelper.ORDERED + " = ?";

        Cursor cursor = database.rawQuery(sql, new String[] { ordered + "" });

        if (cursor.moveToNext()) {
            orders = new Orders();
            orders.setId(cursor.getInt(0));
            orders.setOrdered(cursor.getInt(1)>1);
            orders.setDate_created(cursor.getLong(2));

        }
        return orders;
    }

    public long updateOrder(Orders orders) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.ORDERED, orders.getOrdered()== true ? 1:0);
        values.put(DataBaseHelper.CREATED_AT, System.currentTimeMillis());

        long result = database.update(DataBaseHelper.ORDERS_TABLE, values,
                WHERE_ID_EQUALS,
                new String[] { 0 + "" });
        Log.d("Update Result:", "=" + result);
        return result;
    }
}
