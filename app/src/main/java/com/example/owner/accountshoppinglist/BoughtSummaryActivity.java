package com.example.owner.accountshoppinglist;

import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BoughtSummaryActivity extends AppCompatActivity {
    private static SQLiteDatabase db;
    private static ArrayList<ShoppingItem> boughtList;
    private static int totalPrice;
    public static TextView edit_total_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought_summary);
        ShoppingItemDatabase dbConnection=new ShoppingItemDatabase(BoughtSummaryActivity.this);
        db= dbConnection.openDatabase();
        boughtList=DatabaseUtility.selectAll_bought(db);

        final ListView bought_list_view=findViewById(R.id.list_view_bought);
        edit_total_price=findViewById(R.id.editText_total_price);
        CalculateTotalPrice(boughtList);
        BoughtSummaryListAdapter boughtSummaryListAdapter=
                new BoughtSummaryListAdapter(BoughtSummaryActivity.this,R.layout.bought_item,boughtList,db);
        bought_list_view.setAdapter(boughtSummaryListAdapter);
        boughtSummaryListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                Log.d("onChanged","totalPrice ："+totalPrice+" boughtList size:"+boughtList.size());
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

            }
        });




    }

    public static void CalculateTotalPrice(ArrayList<ShoppingItem> boughtList){
        totalPrice=0;
        for (ShoppingItem s: boughtList
             ) {
            totalPrice+=s.getPrice();
        }


        edit_total_price.setText(totalPrice+"");


    }

}
