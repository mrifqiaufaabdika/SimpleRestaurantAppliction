package moun.com.deli.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import moun.com.deli.model.Cart;
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
    private static final String WHERE_ORDER_EQUALS = DataBaseHelper.ORDERED
            + " =?";

    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    public ItemsDAO(Context context) {
        super(context);
    }

    public long saveToCartTable(Cart cart) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, cart.getItemName());
        values.put(DataBaseHelper.DESCRIPTION_COLOMN, cart.getItemDescription());
        values.put(DataBaseHelper.IMAGE_COLOMN, cart.getItemImage());
        values.put(DataBaseHelper.PRICE_COLOMN, cart.getItemPrice());
        values.put(DataBaseHelper.QUANTITY_COLOMN, cart.getItemQuantity());
        values.put(DataBaseHelper.ORDER_ID, cart.getOrders().getId());

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

    public long updateCartTable(Cart cart) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, cart.getItemName());
        values.put(DataBaseHelper.DESCRIPTION_COLOMN, cart.getItemDescription());
        values.put(DataBaseHelper.IMAGE_COLOMN, cart.getItemImage());
        values.put(DataBaseHelper.PRICE_COLOMN, cart.getItemPrice());
        values.put(DataBaseHelper.QUANTITY_COLOMN, cart.getItemQuantity());
        values.put(DataBaseHelper.ORDER_ID, cart.getOrders().getId());

        long result = database.update(DataBaseHelper.CART_TABLE, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(cart.getId()) });
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

    public int deleteFromCart(Cart cart) {
        return database.delete(DataBaseHelper.CART_TABLE, WHERE_ID_EQUALS,
                new String[] { cart.getId() + "" });
    }



    public int deleteFromFavorites(MenuItems menuItems) {
        return database.delete(DataBaseHelper.FAVORITE_TABLE, WHERE_ID_EQUALS,
                new String[] { menuItems.getId() + "" });
    }

    //USING query() method
    public ArrayList<Cart> getCartItems() {
        ArrayList<Cart> cartItems = new ArrayList<Cart>();

        Cursor cursor = database.query(DataBaseHelper.CART_TABLE,
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
                Cart cart = new Cart();
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

    //USING query() method
    public ArrayList<Cart> getItemsOrderHistory(int id) {
        ArrayList<Cart> cartItems = new ArrayList<Cart>();

        String sql = "SELECT * FROM " + DataBaseHelper.CART_TABLE
                + " WHERE " + DataBaseHelper.ORDER_ID + " = ?";

        String query = "SELECT " + ITEM_ID_WITH_PREFIX + ","
                + ITEM_NAME_WITH_PREFIX + "," + DataBaseHelper.DESCRIPTION_COLOMN
                + "," + DataBaseHelper.IMAGE_COLOMN + ","
                + DataBaseHelper.PRICE_COLOMN + ","
                + DataBaseHelper.QUANTITY_COLOMN + ","
                + DataBaseHelper.ORDER_ID + ","
                + ORDER_NAME_WITH_PREFIX + " FROM "
                + DataBaseHelper.CART_TABLE + " cart INNER JOIN "
                + DataBaseHelper.ORDERS_TABLE + " orders ON cart."
                + DataBaseHelper.ORDER_ID + " = orders."
                + DataBaseHelper.ID_COLUMN
                + " WHERE " + "cart." + DataBaseHelper.ORDER_ID + " = ?";

        Cursor cursor = database.rawQuery(sql, new String[] { id + "" });

        Cursor cursorr = database.query(DataBaseHelper.CART_TABLE,
                new String[] { DataBaseHelper.ID_COLUMN,
                        DataBaseHelper.NAME_COLUMN,
                        DataBaseHelper.DESCRIPTION_COLOMN,
                        DataBaseHelper.IMAGE_COLOMN,
                        DataBaseHelper.PRICE_COLOMN,
                        DataBaseHelper.QUANTITY_COLOMN,
                        DataBaseHelper.ORDER_ID}, null, null, null,
                null, null);

        while (cursor.moveToNext()) {
            Cart cart = new Cart();
            cart.setId(cursor.getInt(0));
            cart.setItemName(cursor.getString(1));
            cart.setItemDescription(cursor.getString(2));
            cart.setItemImage(cursor.getInt(3));
            cart.setItemPrice(cursor.getDouble(4));
            cart.setItemQuantity(cursor.getInt(5));

            Orders orders = new Orders();
            orders.setId(cursor.getInt(6));
        //    orders.setOrdered(cursor.getInt(7)>0);

            cart.setOrders(orders);

            cartItems.add(cart);

        }
        Log.d("GET CART ITEMS:", "=" + cartItems.toString());

        return cartItems;
    }


    // Uses rawQuery() to query multiple tables
    public ArrayList<Cart> getCartItemsss() {
        ArrayList<Cart> cartItems = new ArrayList<Cart>();
        String queryy = "SELECT " + ITEM_ID_WITH_PREFIX + ","
                + ITEM_NAME_WITH_PREFIX + "," + DataBaseHelper.DESCRIPTION_COLOMN
                + "," + DataBaseHelper.IMAGE_COLOMN + ","
                + DataBaseHelper.PRICE_COLOMN + ","
                + DataBaseHelper.QUANTITY_COLOMN + ","
                + DataBaseHelper.ORDER_ID + ","
                + ORDER_NAME_WITH_PREFIX + " FROM "
                + DataBaseHelper.CART_TABLE + " cart, "
                + DataBaseHelper.ORDERS_TABLE + " orders WHERE cart."
                + DataBaseHelper.ORDER_ID + " = orders."
                + DataBaseHelper.ID_COLUMN;

        // Building query using INNER JOIN keyword
		String query = "SELECT " + ITEM_ID_WITH_PREFIX + ","
		+ ITEM_NAME_WITH_PREFIX + "," + DataBaseHelper.DESCRIPTION_COLOMN
                + "," + DataBaseHelper.IMAGE_COLOMN + ","
                + DataBaseHelper.PRICE_COLOMN + ","
                + DataBaseHelper.QUANTITY_COLOMN + ","
                + DataBaseHelper.ORDER_ID + ","
		+ ORDER_NAME_WITH_PREFIX + " FROM "
		+ DataBaseHelper.CART_TABLE + " cart INNER JOIN "
		+ DataBaseHelper.ORDERS_TABLE + " orders ON cart."
		+ DataBaseHelper.ORDER_ID + " = orders."
		+ DataBaseHelper.ID_COLUMN
        + " WHERE " + "orders." + DataBaseHelper.ORDERED + " = ?";


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, new String[] { 0 + "" } );
        while (cursor.moveToNext()) {
            Cart cart = new Cart();
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


    // METHOD 2
    // Uses SQLiteQueryBuilder to query multiple tables
	public ArrayList<Cart> getCartItemss() {
        ArrayList<Cart> cartItems = new ArrayList<Cart>();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		// Sets the list of tables to query. Multiple tables can be specified to perform a join.
        queryBuilder
				.setTables(DataBaseHelper.CART_TABLE
						+ " INNER JOIN "
						+ DataBaseHelper.ORDERS_TABLE
						+ " ON "
						+ DataBaseHelper.ORDER_ID
						+ " = "
						+ (DataBaseHelper.ORDERS_TABLE + "." + DataBaseHelper.ID_COLUMN));

		// Get cursor
		Cursor cursor = queryBuilder.query(database, new String[] {
				ITEM_ID_WITH_PREFIX,
				DataBaseHelper.CART_TABLE + "."
						+ DataBaseHelper.NAME_COLUMN,
				DataBaseHelper.DESCRIPTION_COLOMN,
				DataBaseHelper.IMAGE_COLOMN,
				DataBaseHelper.PRICE_COLOMN,
                DataBaseHelper.QUANTITY_COLOMN,
                DataBaseHelper.ORDER_ID,
				DataBaseHelper.ORDERS_TABLE + "."
						+ DataBaseHelper.NAME_COLUMN }, null, null, null, null,
				null);

        while (cursor.moveToNext()) {
            Cart cart = new Cart();
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
