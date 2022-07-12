package com.zj.android.expensetracker.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;
import com.zj.android.expensetracker.CustomViewModel;
import com.zj.android.expensetracker.DatabaseAccessor;
import com.zj.android.expensetracker.R;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {

    private View mView;
    private CustomViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.activity_dashboard, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
        DatabaseAccessor databaseAccessor = new DatabaseAccessor(requireContext());

        TabLayout tabLayout = mView.findViewById(R.id.graph_tab_layout);
        createAndAddTab(tabLayout, "2022");
//        DatabaseAccessor.getYears();


        // get height of device
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int device_height_px = displayMetrics.heightPixels;
        int device_width_px = displayMetrics.widthPixels;
        float textSize = 14f;

        // setup bar chart data
        List<BarEntry> barEntries = setupBarData();
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setValueFormatter(new CustomValueFormatter());
        barDataSet.setValueTextSize(textSize);
        barDataSet.setColors(setGreenRedColors(barEntries));
        BarData barData = new BarData(barDataSet);

        BarChart barChart = mView.findViewById(R.id.bar_chart);
        barChart.setMinimumHeight(device_height_px / 4 * 3);
        barChart.setMinimumWidth(device_width_px);

        barChart.setData(barData);
        // how many bars are allowed to be seen at once
        barChart.setVisibleXRangeMaximum(5);
        float offset = device_width_px / 100f;
        barChart.setExtraOffsets(offset * 1, 0, offset * 5, 0);

        setBarChartAttributes(barChart);
        barChart.invalidate(); // refresh chart

        // setup pie chart data
        List<PieEntry> pieEntries = setupPieData();
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextSize(textSize);
        PieData pieData = new PieData(pieDataSet);

        PieChart pieChart = mView.findViewById(R.id.pie_chart);
        pieChart.setMinimumHeight(device_height_px);
        pieChart.setData(pieData);
        pieChart.animateXY(2000, 2000);
        // "Description Label" on bottom of graph
        pieChart.getDescription().setEnabled(false);
        // legend on bottom of graph
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate(); // refresh chart
        return mView;
    }

    /*------------------------------ Helper Methods ------------------------------*/
    private void createAndAddTab(TabLayout tabLayout, String year) {
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText(year);
        tabLayout.addTab(tab);
    }

    /*------------------------------ Bar Chart Helper Methods ------------------------------*/
    private List<BarEntry> setupBarData() {
        List<BarEntry> entries = new ArrayList<>();
        // NOTE: Order of the entries added determines their position.
        entries.add(new BarEntry(0, 4.01f));
        entries.add(new BarEntry(1, -2.32f));
        entries.add(new BarEntry(2, 2.33f));
        entries.add(new BarEntry(3, -2.35f));
        entries.add(new BarEntry(4, 4.01f));
        entries.add(new BarEntry(5, -2.32f));
        entries.add(new BarEntry(6, 4.01f));
        entries.add(new BarEntry(7, -2.32f));
        entries.add(new BarEntry(8, 2.33f));
        entries.add(new BarEntry(9, -2.35f));
        entries.add(new BarEntry(10, 4.01f));
        entries.add(new BarEntry(11, -2.32f));
        return entries;
    }

    private int[] setGreenRedColors(List<BarEntry> entries) {
        int[] colors = new int[entries.size()];
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getY() < 0) {
                // red if negative
                colors[i] = Color.rgb(158, 0, 0);
            } else {
                // green if positive
                colors[i] = Color.rgb(0, 128, 0);
            }
        }
        return colors;
    }

    private void setBarChartAttributes(BarChart barChart) {
        barChart.animateXY(2000, 2000);

        barChart.setTouchEnabled(true);
        barChart.setClickable(false);
        // zooming in of graph
        barChart.setDoubleTapToZoomEnabled(false);

        // borders of graph (the line on all 4 sides)
        barChart.setDrawBorders(false);
        // background color of graph
        barChart.setDrawGridBackground(false);

        // "Description Label" on bottom of graph
        barChart.getDescription().setEnabled(false);
        // legend on bottom of graph
        barChart.getLegend().setEnabled(false);

        // horizontal grid line in the chart
        barChart.getAxisLeft().setDrawGridLines(false);
        // left y-axis labels
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setDrawAxisLine(false);

        // vertical grid line in the chart
        barChart.getXAxis().setDrawGridLines(false);
        // top x-axis labels
        barChart.getXAxis().setDrawLabels(false);
        // top x-axis border
        barChart.getXAxis().setDrawAxisLine(false);

        // horizontal grid line in the chart
        barChart.getAxisRight().setDrawGridLines(false);
        // right y-axis labels
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisRight().setDrawAxisLine(false);

        // background of bars
        barChart.setDrawBarShadow(false);
    }

    /*------------------------------ Pie Chart Helper Methods ------------------------------*/
    private List<PieEntry> setupPieData() {
        List<PieEntry> entries = new ArrayList<>();
        // NOTE: Order of the entries added determines their position.
        entries.add(new PieEntry(18.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));
        return entries;
    }

    static class CustomValueFormatter extends ValueFormatter {

        @Override
        public String getBarLabel(BarEntry barEntry) {
            String val = "$" + super.getBarLabel(barEntry);
            if (barEntry.getY() < 0) val = "-$" + (barEntry.getY() * -1);
            return getMonth((int) barEntry.getX()) + ": " + val;
        }

        private String getMonth(int month) {
            switch (month) {
                case 0:
                    return "January";
                case 1:
                    return "February";
                case 2:
                    return "March";
                case 3:
                    return "April";
                case 4:
                    return "May";
                case 5:
                    return "June";
                case 6:
                    return "July";
                case 7:
                    return "August";
                case 8:
                    return "September";
                case 9:
                    return "October";
                case 10:
                    return "November";
                case 11:
                    return "December";
                default:
                    return "";
            }

        }
    }
}