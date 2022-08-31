package moun.com.deli.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import moun.com.deli.model.Items;
import moun.com.deli.model.MenuItems;
import moun.com.deli.model.Orders;

/**
 * This Class using SQLiteDatabase object provides methods for SQLite CRUD
 * (Create, Read, Update, Delete) operations.
 */
public class ItemsDAO extends ItemsDBDAO {
    public static final String ITEM_ID_WITH_PREFIX = "cart.id";
    public static final String ITEM_NAME_WITH_PREFIX = "cart.name";
    public static final String ORDER_NAME_WITH_PREFIX = "orders.ordered";

    private static final String WHERE_ID_EQUALS = DataBaseHelper.ID_COLUMN
            + " =?";

    public ItemsDAO(Context context) {
        super(context);
    }

    public long saveToItemsTable(Items items) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, items.getItemName());
        values.put(DataBaseHelper.DESCRIPTION_COLOMN, items.getItemDescription());
        values.put(DataBaseHelper.IMAGE_COLOMN, items.getItemImage());
        values.put(DataBaseHelper.PRICE_COLOMN, items.getItemPrice());
        values.put(DataBaseHelper.QUANTITY_COLOMN, items.getItemQuantity());
        values.put(DataBaseHelper.ORDER_ID, items.getOrders().getId());

        return database.insert(DataBaseHelper.ITEMS_TABLE, null, values);
    }

    public long saveToFavoriteTable(MenuItems menuItems) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, menuItems.getItemName());
        values.put(DataBaseHelper.DESCRIPTION_COLOMN, menuItems.getItemDescription());
        values.put(DataBaseHelper.IMAGE_COLOMN, menuItems.getItemImage());
        values.put(DataBaseHelper.PRICE_COLOMN, menuItems.getItemPrice());

        return database.insert(DataBaseHelper.FAVORITE_TABLE, null, values);
    }

    public long updateItemsTable(Items items) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, items.getItemName());
        values.put(DataBaseHelper.DESCRIPTION_COLOMN, items.getItemDescription());
        values.put(DataBaseHelper.IMAGE_COLOMN, items.getItemImage());
        values.put(DataBaseHelper.PRICE_COLOMN, items.getItemPrice());
        values.put(DataBaseHelper.QUANTITY_COLOMN, items.getItemQuantity());
        values.put(DataBaseHelper.ORDER_ID, items.getOrders().getId());

        long result = database.update(DataBaseHelper.ITEMS_TABLE, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(items.getId()) });
        Log.d("Update Result:", "=" + result);
        return result;

    }

    public long updateFavoriteTable(MenuItems menuItems) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, menuItems.getItemName());
        values.put(DataBaseHelper.DESCRIPTION_COLOMN, menuItems.getItemDescription());
        values.put(DataBaseHelper.IMAGE_COLOMN, menuItems.getItemImage());
        values.put(DataBaseHelper.PRICE_COLOMN, menuItems.getItemPrice());

        long result = database.update(DataBaseHelper.FAVORITE_TABLE, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(menuItems.getId()) });
        Log.d("Update Result:", "=" + result);
        return result;

    }

    public int deleteFromItemsTable(Items cart) {
        return database.delete(DataBaseHelper.ITEMS_TABLE, WHERE_ID_EQUALS,
                new String[] { cart.getId() + "" });
    }



    public int deleteFromFavorites(MenuItems menuItems) {
        return database.delete(DataBaseHelper.FAVORITE_TABLE, WHERE_ID_EQUALS,
                new String[] { menuItems.getId() + "" });
    }

    //USING query() method
    public ArrayList<Items> getAllCartItems() {
        ArrayList<Items> cartItems = new ArrayList<Items>();

        Cursor cursor = database.query(DataBaseHelper.ITEMS_TABLE,
                new String[] { DataBaseHelper.ID_COLUMN,
                        DataBaseHelper.NAME_COLUMN,
                        DataBaseHelper.DESCRIPTION_COLOMN,
                        DataBaseHelper.IMAGE_COLOMN,
                        DataBaseHelper.PRICE_COLOMN,
                        DataBaseHelper.QUANTITY_COLOMN,
                        DataBaseHelper.ORDER_ID}, null, null, null,
                null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Items cart = new Items();
                cart.setId(cursor.getInt(0));
                cart.setItemName(cursor.getString(1));
                cart.setItemDescription(cursor.getString(2));
                cart.setItemImage(cursor.getInt(3));
                cart.setItemPrice(cursor.getDouble(4));
                cart.setItemQuantity(cursor.getInt(5));
                Orders orders = new Orders();
                orders.setId(cursor.getInt(6));
                orders.setOrdered(cursor.getInt(7)>0);
                orders.setDate_created(cursor.getLong(8));
                cart.setOrders(orders);

                cartItems.add(cart);
            }
        }
        Log.d("GET CART ITEMS:", "=" + cartItems.toString());

        return cartItems;
    }

    // Uses rawQuery() to query multiple tables
    public ArrayList<Items> getCartItemsNotOrdered() {
        ArrayList<Items> cartItems = new ArrayList<Items>();

        // Building query using INNER JOIN keyword
        String query = "SELECT " + ITEM_ID_WITH_PREFIX + ","
                + ITEM_NAME_WITH_PREFIX + "," + DataBaseHelper.DESCRIPTION_COLOMN
                + "," + DataBaseHelper.IMAGE_COLOMN + ","
                + DataBaseHelper.PRICE_COLOMN + ","
                + DataBaseHelper.QUANTITY_COLOMN + ","
                + DataBaseHelper.ORDER_ID + ","
                + ORDER_NAME_WITH_PREFIX + " FROM "
                + DataBaseHelper.ITEMS_TABLE + " cart INNER JOIN "
                + DataBaseHelper.ORDERS_TABLE + " orders ON cart."
                + DataBaseHelper.ORDER_ID + " = orders."
                + DataBaseHelper.ID_COLUMN
                + " WHERE " + "orders." + DataBaseHelper.ORDERED + " = ?";


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, new String[] { 0 + "" } );
        while (cursor.moveToNext()) {
            Items cart = new Items();
            cart.setId(cursor.getInt(0));
            cart.setItemName(cursor.getString(1));
            cart.setItemDescription(cursor.getString(2));
            cart.setItemImage(cursor.getInt(3));
            cart.setItemPrice(cursor.getDouble(4));
            cart.setItemQuantity(cursor.getInt(5));

            Orders orders = new Orders();
            orders.setId(cursor.getInt(6));
            orders.setOrdered(cursor.getInt(7)>0);

            cart.setOrders(orders);

            cartItems.add(cart);

        }
        Log.d("GET CART ITEMS:", "=" + cartItems);
        return cartItems;
    }

    //USING query() method
    public ArrayList<Items> getItemsOrderHistory(int id) {
        ArrayList<Items> cartItems = new ArrayList<Items>();

        String sql = "SELECT * FROM " + DataBaseHelper.ITEMS_TABLE
                + " WHERE " + DataBaseHelper.ORDER_ID + " = ?";

        Cursor cursor = database.rawQuery(sql, new String[] { id + "" });

        while (cursor.moveToNext()) {
            Items cart = new Items();
            cart.setId(cursor.getInt(0));
            cart.setItemName(cursor.getString(1));
            cart.setItemDescription(cursor.getString(2));
            cart.setItemImage(cursor.getInt(3));
            cart.setItemPrice(cursor.getDouble(4));
            cart.setItemQuantity(cursor.getInt(5));

            Orders orders = new Orders();
            orders.setId(cursor.getInt(6));

            cart.setOrders(orders);

            cartItems.add(cart);

        }
        Log.d("GET CART ITEMS:", "=" + cartItems.toString());

        return cartItems;
    }

    //USING query() method
    public ArrayList<MenuItems> getFavoriteItems() {
        ArrayList<MenuItems> cartItems = new ArrayList<MenuItems>();

        Cursor cursor = database.query(DataBaseHelper.FAVORITE_TABLE,
                new String[] { DataBaseHelper.ID_COLUMN,
                        DataBaseHelper.NAME_COLUMN,
                        DataBaseHelper.DESCRIPTION_COLOMN,
                        DataBaseHelper.IMAGE_COLOMN,
                        DataBaseHelper.PRICE_COLOMN }, null, null, null,
                null, null);

        while (cursor.moveToNext()) {
            MenuItems menuItems = new MenuItems();
            menuItems.setId(cursor.getInt(0));
            menuItems.setItemName(cursor.getString(1));
            menuItems.setItemDescription(cursor.getString(2));
            menuItems.setItemImage(cursor.getInt(3));
            menuItems.setItemPrice(cursor.getDouble(4));

            cartItems.add(menuItems);
        }
        return cartItems;
    }

    //Retrieves a single cart item record with the given id
    public MenuItems getItemCart(long id) {
        MenuItems menuItems = null;

        String sql = "SELECT * FROM " + DataBaseHelper.ITEMS_TABLE
                + " WHERE " + DataBaseHelper.ID_COLUMN + " = ?";

        Cursor cursor = database.rawQuery(sql, new String[] { id + "" });

        if (cursor.moveToNext()) {
            menuItems = new MenuItems();
            menuItems.setId(cursor.getInt(0));
            menuItems.setItemName(cursor.getString(1));
            menuItems.setItemDescription(cursor.getString(2));
            menuItems.setItemImage(cursor.getInt(3));
            menuItems.setItemPrice(cursor.getDouble(4));
            menuItems.setItemQuantity(cursor.getInt(5));
        }
        return menuItems;
    }

    //Retrieves a single favorite item record with the item name
    public MenuItems getItemFavorite(String title) {
        MenuItems menuItems = null;

        String sql = "SELECT * FROM " + DataBaseHelper.FAVORITE_TABLE
                + " WHERE " + DataBaseHelper.NAME_COLUMN + " = ?";

        Cursor cursor = database.rawQuery(sql, new String[] { title + "" });

        if (cursor.moveToNext()) {
            menuItems = new MenuItems();
            menuItems.setId(cursor.getInt(0));
            menuItems.setItemName(cursor.getString(1));
            menuItems.setItemDescription(cursor.getString(2));
            menuItems.setItemImage(cursor.getInt(3));
            menuItems.setItemPrice(cursor.getDouble(4));

        }
        return menuItems;
    }

}
