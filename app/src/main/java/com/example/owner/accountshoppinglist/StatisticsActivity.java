package com.example.owner.accountshoppinglist;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;

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
        GraphView graph=findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);
    }
}
