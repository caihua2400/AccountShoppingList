package com.example.owner.accountshoppinglist;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static SQLiteDatabase db;
    private static ArrayList<ShoppingItem> wishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShoppingItemDatabase dbConnection=new ShoppingItemDatabase(this);
        db= dbConnection.openDatabase();
        List<ShoppingItem> shoppingItems=  DatabaseUtility.selectAll(db);
        Button button_add_item= findViewById(R.id.button_add);
        button_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddItemActivity.class);
                startActivity(intent);
            }
        });
        Button button_turnto_bought=findViewById(R.id.button_turnTo_bought);
        button_turnto_bought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,BoughtSummaryActivity.class);
                startActivity(intent);
            }
        });

        ListView listView_main= findViewById(R.id.list_view_main);
        SearchView searchView_main=findViewById(R.id.search_name);
        final MainListAdapter mainListAdapter=new MainListAdapter(MainActivity.this,R.layout.shopping_item,shoppingItems,db);
        listView_main.setAdapter(mainListAdapter);
        searchView_main.setSubmitButtonEnabled(true);
        searchView_main.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mainListAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mainListAdapter.getFilter().filter(s);
                return false;
            }
        });


    }


}
