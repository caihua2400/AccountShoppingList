package com.example.owner.accountshoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;

public class QueryTask extends AsyncTask<Void,Void,ArrayList<ShoppingItem>>{
    private Context context;
    private static SQLiteDatabase db;
    public QueryTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<ShoppingItem> doInBackground(Void... voids) {
        ShoppingItemDatabase dbConnection=new ShoppingItemDatabase(context);
        db=dbConnection.openDatabase();
        ArrayList<ShoppingItem> shoppingItems=DatabaseUtility.selectAll(db);
        return shoppingItems;
    }
}