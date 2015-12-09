package moun.com.deli.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;

import moun.com.deli.model.MenuItems;

/**
 * This Class using SQLiteDatabase object provides methods for SQLite CRUD
 * (Create, Read, Update, Delete) operations.
 */
public class ItemsDAO extends ItemsDBDAO {
    private static final String WHERE_ID_EQUALS = DataBaseHelper.ID_COLUMN
            + " =?";

    public ItemsDAO(Context context) {
        super(context);
    }

    public long saveToCartTable(MenuItems menuItems) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, menuItems.getItemName());
        values.put(DataBaseHelper.DESCRIPTION_COLOMN, menuItems.getItemDescription());
        values.put(DataBaseHelper.IMAGE_COLOMN, menuItems.getItemImage());
        values.put(DataBaseHelper.PRICE_COLOMN, menuItems.getItemPrice());
        values.put(DataBaseHelper.QUANTITY_COLOMN, menuItems.getItemQuantity());

        return database.insert(DataBaseHelper.CART_TABLE, null, values);
    }

    public long saveToFavoriteTable(MenuItems menuItems) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, menuItems.getItemName());
        values.put(DataBaseHelper.DESCRIPTION_COLOMN, menuItems.getItemDescription());
        values.put(DataBaseHelper.IMAGE_COLOMN, menuItems.getItemImage());
        values.put(DataBaseHelper.PRICE_COLOMN, menuItems.getItemPrice());

        return database.insert(DataBaseHelper.FAVORITE_TABLE, null, values);
    }

    public long updateCartTable(MenuItems menuItems) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, menuItems.getItemName());
        values.put(DataBaseHelper.DESCRIPTION_COLOMN, menuItems.getItemDescription());
        values.put(DataBaseHelper.IMAGE_COLOMN, menuItems.getItemImage());
        values.put(DataBaseHelper.PRICE_COLOMN, menuItems.getItemPrice());
        values.put(DataBaseHelper.QUANTITY_COLOMN, menuItems.getItemQuantity());

        long result = database.update(DataBaseHelper.CART_TABLE, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(menuItems.getId()) });
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

    public int deleteFromCart(MenuItems menuItems) {
        return database.delete(DataBaseHelper.CART_TABLE, WHERE_ID_EQUALS,
                new String[] { menuItems.getId() + "" });
    }



    public int deleteFromFavorites(MenuItems menuItems) {
        return database.delete(DataBaseHelper.FAVORITE_TABLE, WHERE_ID_EQUALS,
                new String[] { menuItems.getId() + "" });
    }

    //USING query() method
    public ArrayList<MenuItems> getCartItems() {
        ArrayList<MenuItems> cartItems = new ArrayList<MenuItems>();

        Cursor cursor = database.query(DataBaseHelper.CART_TABLE,
                new String[] { DataBaseHelper.ID_COLUMN,
                        DataBaseHelper.NAME_COLUMN,
                        DataBaseHelper.DESCRIPTION_COLOMN,
                        DataBaseHelper.IMAGE_COLOMN,
                        DataBaseHelper.PRICE_COLOMN,
                        DataBaseHelper.QUANTITY_COLOMN }, null, null, null,
                null, null);

        while (cursor.moveToNext()) {
            MenuItems menuItems = new MenuItems();
            menuItems.setId(cursor.getInt(0));
            menuItems.setItemName(cursor.getString(1));
            menuItems.setItemDescription(cursor.getString(2));
            menuItems.setItemImage(cursor.getInt(3));
            menuItems.setItemPrice(cursor.getDouble(4));
            menuItems.setItemQuantity(cursor.getInt(5));

            cartItems.add(menuItems);
        }
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

        String sql = "SELECT * FROM " + DataBaseHelper.CART_TABLE
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

    //Retrieves a single favorite item record with the given id
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
