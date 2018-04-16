package com.example.owner.accountshoppinglist;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public static SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShoppingItemDatabase dbConnection=new ShoppingItemDatabase(this);
        db= dbConnection.openDatabase();
    }
}
