package com.zj.android.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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

        // get height of device
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int device_height_px = displayMetrics.heightPixels;

        // setup bar chart
        List<BarEntry> barEntries = setupBarData();
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);

        BarChart barChart = findViewById(R.id.bar_chart);
        barChart.setMinimumHeight(device_height_px);
        barChart.setData(barData);
        barChart.animateXY(2000,2000);
        barChart.invalidate(); // refresh chart

        // setup pie chart
        List<PieEntry> pieEntries= setupPieData();
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);

        PieChart pieChart = findViewById(R.id.pie_chart);
        pieChart.setMinimumHeight(device_height_px);
        pieChart.setData(pieData);
        pieChart.animateXY(2000,2000);
        pieChart.invalidate(); // refresh chart
    }

    /************************* Bar Chart Helper Methods ***************************************/
    private List<BarEntry> setupBarData(){
        List<BarEntry> entries = new ArrayList<>();
        // NOTE: Order of the entries added determines their position.
        entries.add(new BarEntry(1, 2.3f));
        entries.add(new BarEntry(2, -2.3f));
        entries.add(new BarEntry(3, 2.3f));
        entries.add(new BarEntry(4, -2.3f));
        return entries;
    }
    /************************* Pie Chart Helper Methods ***************************************/
    private List<PieEntry> setupPieData(){
        List<PieEntry> entries = new ArrayList<>();
        // NOTE: Order of the entries added determines their position.
        entries.add(new PieEntry(18.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));
        return entries;
    }
}