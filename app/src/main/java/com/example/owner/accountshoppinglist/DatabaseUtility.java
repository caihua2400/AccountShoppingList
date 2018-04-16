package com.example.owner.accountshoppinglist;

/**
 * Created by Owner on 16/04/2018.
 */

public class DatabaseUtility {
    public static final String TABLE_NAME="shoppingItem";
    public static final String KEY_SHOPPINGITEM_ID="shoppingItem_id";
    public static final String KEY_NAME="name";
    public static final String KEY_PRICE="price";
    public static final String KEY_QUANTITY="quantity";
    public static final String TABLE_NAME_BOUGHT="shoppingItem_bought";
    public static final String KEY_ITEM_PHOTO_PATH="item_path";
    public static final String KEY_TAG="tag";
    public static final String CREATE_STATEMENT="CREATE TABLE"
            +" "
            +TABLE_NAME
            + " (" + KEY_SHOPPINGITEM_ID + " integer primary key autoincrement, "
            + KEY_NAME + " string not null, "
            + KEY_PRICE + " int not null, "
            + KEY_QUANTITY + " integer not null, "
            + KEY_ITEM_PHOTO_PATH+ " string not null, "
            + KEY_TAG+ " string not null"
            +");";
    public static final String CREATE_STATEMENT_BOUGHT="CREATE TABLE"
            +" "
            +TABLE_NAME_BOUGHT
            + " (" + KEY_SHOPPINGITEM_ID + " integer primary key autoincrement, "
            + KEY_NAME + " string not null, "
            + KEY_PRICE + " int not null, "
            + KEY_QUANTITY + " integer not null, "
            + KEY_ITEM_PHOTO_PATH+ " string not null, "
            + KEY_TAG+" string not null"
            +");";
}
