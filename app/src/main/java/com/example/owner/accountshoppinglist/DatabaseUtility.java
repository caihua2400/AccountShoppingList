package com.example.owner.accountshoppinglist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

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
    public static final String TABLE_NAME_NAMELIST="namelist";

    public static final String CREATE_STATEMENT_NAMELIST="CREATE TABLE"
            +" "
            +TABLE_NAME_NAMELIST
            +" ("+KEY_SHOPPINGITEM_ID+" integer primary key autoincrement, "
            +KEY_NAME+" string not null "
            +");";

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

    public static void insert(SQLiteDatabase db, ShoppingItem s){
        ContentValues values=new ContentValues();
        values.put(KEY_NAME,s.getName());
        values.put(KEY_PRICE,s.getPrice());
        values.put(KEY_QUANTITY,s.getQuantity());
        values.put(KEY_ITEM_PHOTO_PATH,s.getPath());
        values.put(KEY_TAG,s.getTag());
        db.insert(TABLE_NAME,null,values);
    }
    public static void insert_bought(SQLiteDatabase db,ShoppingItem s){
        ContentValues values=new ContentValues();
        values.put(KEY_NAME,s.getName());
        values.put(KEY_PRICE,s.getPrice());
        values.put(KEY_QUANTITY,s.getQuantity());
        values.put(KEY_ITEM_PHOTO_PATH,s.getPath());
        values.put(KEY_TAG,s.getTag());
        db.insert(TABLE_NAME_BOUGHT,null,values);
    }

    public static void insert_nameList(SQLiteDatabase db,String name){
        boolean flag=false;
        ArrayList<String> nameList=selectAllName(db);
        for (String thisName: nameList
             ) {
            if(thisName==name){
                flag=true;
                Log.d("insert_nameList",flag+"");
                break;
            }

        }
        if(flag==false) {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, name);
            db.insert(TABLE_NAME_NAMELIST, null, values);
        }
    }

    public static void update(SQLiteDatabase db,ShoppingItem s){
        ContentValues values=new ContentValues();
        values.put(KEY_NAME,s.getName());
        values.put(KEY_PRICE,s.getPrice());
        values.put(KEY_QUANTITY,s.getQuantity());
        values.put(KEY_ITEM_PHOTO_PATH,s.getPath());
        values.put(KEY_TAG,s.getTag());
        db.update(TABLE_NAME,values,KEY_SHOPPINGITEM_ID+"= ?",new String[]{""+s.getId()});

    }
    public static void delete(SQLiteDatabase db, ShoppingItem s){
        db.delete(TABLE_NAME,KEY_SHOPPINGITEM_ID+"= ?",new String[]{""+s.getId()});

    }
    public static void delete_bought(SQLiteDatabase db,ShoppingItem s){
        db.delete(TABLE_NAME_BOUGHT,KEY_SHOPPINGITEM_ID+"= ?",new String[]{""+s.getId()});
    }
    public static ArrayList<String> selectAllName(SQLiteDatabase db){
        ArrayList<String> result=new ArrayList<>();
        Cursor c= db.query(TABLE_NAME_NAMELIST,null,null,null,null,null,null);
        if(c!=null){
            c.moveToNext();
            while(!c.isAfterLast()){
                String p=c.getString(c.getColumnIndex(KEY_NAME));
                result.add(p);
                c.moveToNext();
            }
        }
        return result;
    }
    public static ArrayList<ShoppingItem> selectAll(SQLiteDatabase db){
        ArrayList<ShoppingItem> results=new ArrayList<ShoppingItem>();
        Cursor c= db.query(TABLE_NAME,null,null,null,null,null,null);
        if(c!=null){
            c.moveToNext();
            while(!c.isAfterLast()){
                ShoppingItem p=createFromCursor(c);
                results.add(p);
                c.moveToNext();
            }
        }
        return results;
    }
    public static ArrayList<ShoppingItem> selectAll_bought(SQLiteDatabase db){
        ArrayList<ShoppingItem> results=new ArrayList<ShoppingItem>();
        Cursor c= db.query(TABLE_NAME_BOUGHT,null,null,null,null,null,null);
        if(c!=null){
            c.moveToNext();
            while(!c.isAfterLast()){
                ShoppingItem p=createFromCursor(c);
                results.add(p);
                c.moveToNext();
            }
        }
        return results;
    }
    private static ShoppingItem createFromCursor(Cursor c) {
        if(c==null || c.isAfterLast() || c.isBeforeFirst()){
            return null;
        }else{
            ShoppingItem p=new ShoppingItem();
            p.setId(c.getInt(c.getColumnIndex(KEY_SHOPPINGITEM_ID)));
            p.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            p.setPrice(c.getInt(c.getColumnIndex(KEY_PRICE)));
            p.setQuantity(c.getInt(c.getColumnIndex(KEY_QUANTITY)));
            p.setPath(c.getString(c.getColumnIndex(KEY_ITEM_PHOTO_PATH)));
            p.setTag(c.getString(c.getColumnIndex(KEY_TAG)));
            return p;

        }
    }
}
