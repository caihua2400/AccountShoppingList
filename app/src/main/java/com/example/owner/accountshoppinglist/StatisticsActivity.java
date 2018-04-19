package com.example.owner.accountshoppinglist;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {
    private static SQLiteDatabase db;
    private static ArrayList<ShoppingItem> boughtList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ShoppingItemDatabase dbConnection=new ShoppingItemDatabase(StatisticsActivity.this);
        db= dbConnection.openDatabase();
        ArrayList<ShoppingItem> Bought_list=DatabaseUtility.selectAll_bought(db);
    }
}
