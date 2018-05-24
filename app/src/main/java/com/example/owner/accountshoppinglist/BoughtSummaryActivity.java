package com.example.owner.accountshoppinglist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class BoughtSummaryActivity extends AppCompatActivity {
    private static SQLiteDatabase db;
    private static ArrayList<ShoppingItem> boughtList;
    private static int totalPrice;
    public static TextView edit_total_price;
    mBroadcastReceiver mReceiver;
    private BoughtSummaryListAdapter boughtSummaryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought_summary);
        mReceiver=new mBroadcastReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("update");
        registerReceiver(mReceiver,intentFilter);
        ShoppingItemDatabase dbConnection=new ShoppingItemDatabase(BoughtSummaryActivity.this);
        db= dbConnection.openDatabase();
        boughtList=DatabaseUtility.selectAll_bought(db);

        final ListView bought_list_view=findViewById(R.id.list_view_bought);
        edit_total_price=findViewById(R.id.editText_total_price);

        Button button=findViewById(R.id.button_back_to_main);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BoughtSummaryActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        CalculateTotalPrice(boughtList);
        boughtSummaryListAdapter =
                new BoughtSummaryListAdapter(BoughtSummaryActivity.this,R.layout.bought_item,boughtList,db);

        bought_list_view.setAdapter(boughtSummaryListAdapter);
        boughtSummaryListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                Log.d("onChanged","totalPrice ："+totalPrice+" boughtList size:"+boughtList.size());
                Intent intent=new Intent();
                intent.setAction("update");
                sendBroadcast(intent);
                /*
                totalPrice=0;
                for (ShoppingItem s: boughtList
                        ) {
                    totalPrice+=s.getPrice();
                    edit_total_price.setText(totalPrice+"");
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
              */

            }
        });

         SearchView search_tag_bought= findViewById(R.id.searchView_bought_list);
         search_tag_bought.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String s) {
                 boughtSummaryListAdapter.getFilter().filter(s);
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String s) {
                 boughtSummaryListAdapter.getFilter().filter(s);
                 return false;
             }
         });

        Button button_share= findViewById(R.id.button_share);
        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareWhatsInTheBox();
            }
        });

        Button button_to_statistics=findViewById(R.id.button_to_statistics);
        button_to_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BoughtSummaryActivity.this,StatisticsActivity.class);
                startActivity(intent);
            }
        });




    }
    void shareWhatsInTheBox()
    {
        String shoppingList="";
        for (ShoppingItem s:boughtList
             ) {
            shoppingList+="Name : "+s.getName()+" Price: "+s.getPrice()+" Quantity :"+s.getQuantity() +"\n";

        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shoppingList);
        sendIntent.setType("text/plain");


        startActivity(Intent.createChooser(sendIntent, "Share via..."));
    }

    public static void CalculateTotalPrice(ArrayList<ShoppingItem> boughtList){
        totalPrice=0;
        for (ShoppingItem s: boughtList
             ) {
            totalPrice+=s.getPrice();
        }


        edit_total_price.setText(totalPrice+"");


    }
    private class mBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            totalPrice=0;
            for (ShoppingItem s: boughtSummaryListAdapter.boughtList
                    ) {
                totalPrice+=s.getPrice();
                edit_total_price.setText(totalPrice+"");
            }
            if(boughtSummaryListAdapter.boughtList.size()==0){
                edit_total_price.setText(0+"");
            }
            Log.d("onReceive","updated");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("update");
        registerReceiver(mReceiver,intentFilter);
        Log.d("onResume","registered");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
        Log.d("onPause","received");
    }
}
