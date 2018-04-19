package com.example.owner.accountshoppinglist;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class BoughtSummaryActivity extends AppCompatActivity {
    private static SQLiteDatabase db;
    private static ArrayList<ShoppingItem> boughtList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought_summary);
        ShoppingItemDatabase dbConnection=new ShoppingItemDatabase(BoughtSummaryActivity.this);
        db= dbConnection.openDatabase();
        boughtList=DatabaseUtility.selectAll_bought(db);
        final ListView bought_list_view=findViewById(R.id.list_view_bought);
        BoughtSummaryListAdapter boughtSummaryListAdapter=
                new BoughtSummaryListAdapter(BoughtSummaryActivity.this,R.layout.bought_item,boughtList,db);
        bought_list_view.setAdapter(boughtSummaryListAdapter);
    }
}
