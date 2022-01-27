package com.zj.android.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
//import com.anychart.AnyChart;
//import com.anychart.AnyChartView;
//import com.anychart.chart.common.dataentry.ValueDataEntry;
//import com.anychart.charts.Pie;
//import com.anychart.chart.common.dataentry.DataEntry;
//
//import java.util.ArrayList;
//import java.util.List;


public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // in this example, a LineChart is initialized from xml
        PieChart chart = findViewById(R.id.chart);

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(18.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));
        PieDataSet set = new PieDataSet(entries, "");
        PieData data = new PieData(set);
        chart.setData(data);
        chart.invalidate(); // refresh
    }
}