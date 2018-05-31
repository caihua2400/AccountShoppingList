package com.example.owner.accountshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static SQLiteDatabase db;
    private static ArrayList<ShoppingItem> wishlist;
    private ArrayList<ShoppingItem> shoppingItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
             shoppingItems = new queryDatabaseTask(MainActivity.this).execute(MainActivity.this).get();
        }catch (Exception e){
            Log.d("MainActivity",e.toString());
        };
        /*
        ShoppingItemDatabase dbConnection=new ShoppingItemDatabase(this);
        db= dbConnection.openDatabase();
        */
        //List<ShoppingItem> shoppingItems=  DatabaseUtility.selectAll(db);
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

    private class queryDatabaseTask extends AsyncTask<Context,Void,ArrayList<ShoppingItem>>{
        private Context context;

        public queryDatabaseTask(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<ShoppingItem> doInBackground(Context... contexts) {
            ShoppingItemDatabase dbConnection=new ShoppingItemDatabase(contexts[0]);
            db=dbConnection.openDatabase();
            ArrayList<ShoppingItem> shoppingItems=DatabaseUtility.selectAll(db);
            return shoppingItems;
        }

        @Override
        protected void onPostExecute(ArrayList<ShoppingItem> shoppingItems) {
            Toast.makeText(context,"query completed",Toast.LENGTH_SHORT).show();
        }
    }


}
