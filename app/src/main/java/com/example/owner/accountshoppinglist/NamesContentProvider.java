package com.example.owner.accountshoppinglist;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.owner.accountshoppinglist.DatabaseUtility.TABLE_NAME_NAMELIST;

public class NamesContentProvider extends ContentProvider {
    static final String PROVIDER_NAME="com.example.owner.accountshoppinglist.NamesContentProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/names";
    static final Uri CONTENT_URI = Uri.parse(URL);
    private static SQLiteDatabase db;
    static final int NAMES=1;


    private static UriMatcher uriMatcher ;
    static{
        uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "names", NAMES);



    }

    @Override
    public boolean onCreate() {
        Context context=getContext();
        ShoppingItemDatabase dbConnection=new ShoppingItemDatabase(context);
        db=dbConnection.openDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor c;
        if(uriMatcher.match(uri)==1) {


            c = db.query(TABLE_NAME_NAMELIST, null, null, null, null, null, null);
            Log.d("contentProvider","query complete"+c.toString());
            return c;
        }else{
            return null;
        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if(uriMatcher.match(uri)==1){

            //db.insert();
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
