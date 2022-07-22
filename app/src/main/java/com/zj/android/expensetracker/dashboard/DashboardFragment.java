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
import com.zj.android.expensetracker.R;
import com.zj.android.expensetracker.database.DatabaseAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DashboardFragment extends Fragment {

    private final float TEXT_SIZE = 14f;
    private View mView;
    private CustomViewModel mViewModel;
    private String mSelectedYear;
    private BarChart mBarChart;
    private PieChart mPieChart;
    private TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.activity_dashboard, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
        DatabaseAccessor databaseAccessor = new DatabaseAccessor(requireContext());

        // populate years in the tab layout at the top
        mTabLayout = mView.findViewById(R.id.graph_tab_layout);
        List<String> years = DatabaseAccessor.getYears();
        for (String year : years) {
            createAndAddTab(mTabLayout, year);
        }
        if (years.size() > 0) mSelectedYear = years.get(0);

        // get dimensions of device
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int device_height_px = displayMetrics.heightPixels,
                device_width_px = displayMetrics.widthPixels;

        // setup bar chart
        mBarChart = mView.findViewById(R.id.bar_chart);
        setupBarChart(device_height_px, device_width_px);

        // setup pie chart data
        mPieChart = mView.findViewById(R.id.pie_chart);
        setupPieChart(device_height_px);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mSelectedYear = tab.getText().toString(); // update year
                mBarChart.setData(setupBarData());
                mBarChart.invalidate(); // refresh chart
                mPieChart.setData(setupPieData());
                mPieChart.invalidate(); // refresh chart
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<String> years = DatabaseAccessor.getYears();
        if (years.size() != mTabLayout.getTabCount()) {
            mTabLayout.removeAllTabs();
            for (String year : years) {
                createAndAddTab(mTabLayout, year);
            }
            if (years.size() > 0) mSelectedYear = years.get(0);
        }
        mBarChart.setData(setupBarData());
        mBarChart.invalidate(); // refresh chart
        mPieChart.setData(setupPieData());
        mPieChart.invalidate(); // refresh chart
    }

    /*------------------------------ Helper Methods ------------------------------*/
    private void createAndAddTab(TabLayout tabLayout, String year) {
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText(year);
        tabLayout.addTab(tab);
    }

    /*------------------------------ Bar Chart Helper Methods ------------------------------*/
    private void setupBarChart(int device_height_px, int device_width_px) {
        BarData barData = setupBarData();
        mBarChart.setMinimumHeight(device_height_px / 4 * 3);
        mBarChart.setMinimumWidth(device_width_px);

        mBarChart.setData(barData);
        // how many bars are allowed to be seen at once
        mBarChart.setVisibleXRangeMaximum(5);
        float offset = device_width_px / 100f;
        mBarChart.setExtraOffsets(offset * 1, 0, offset * 5, 0);

        setBarChartAttributes(mBarChart);
        mBarChart.invalidate(); // refresh chart
    }

    private BarData setupBarData() {
        List<BarEntry> barEntries = new ArrayList<>();
        // NOTE: Order of the entries added determines their position.
        for (int i = 0; i < 12; i++) {
            String period = String.format(Locale.getDefault(), "%s-%02d", mSelectedYear, i + 1);
            barEntries.add(new BarEntry(i, DatabaseAccessor.getExpenseAmount(period)));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setValueFormatter(new CustomValueFormatter());
        barDataSet.setValueTextSize(TEXT_SIZE);
        barDataSet.setColors(setGreenRedColors(barEntries));

        return new BarData(barDataSet);
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
    private void setupPieChart(int device_height_px) {
        mPieChart.setMinimumHeight(device_height_px);
        mPieChart.setData(setupPieData());
        mPieChart.animateXY(2000, 2000);
        // "Description Label" on bottom of graph
        mPieChart.getDescription().setEnabled(false);
        // legend on bottom of graph
        mPieChart.getLegend().setEnabled(false);
        mPieChart.invalidate(); // refresh chart
    }

    private PieData setupPieData() {
        List<PieEntry> pieEntries = DatabaseAccessor.getPieData(mSelectedYear);
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "hmm");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextSize(TEXT_SIZE);
        return new PieData(pieDataSet);
    }

    private static class CustomValueFormatter extends ValueFormatter {

        @Override
        public String getBarLabel(BarEntry barEntry) {
            String val = barEntry.getY() < 0 ?
                    String.format(Locale.getDefault(), "-$%.2f", barEntry.getY() * -1) :
                    String.format(Locale.getDefault(), "$%.2f", barEntry.getY());
            return getMonth((int) barEntry.getX()) + ": " + val;
        }

        private String getMonth(int month) {
            switch (month) {
                case 0:
                    return "Jan";
                case 1:
                    return "Feb";
                case 2:
                    return "Mar";
                case 3:
                    return "Apr";
                case 4:
                    return "May";
                case 5:
                    return "Jun";
                case 6:
                    return "Jul";
                case 7:
                    return "Aug";
                case 8:
                    return "Sept";
                case 9:
                    return "Oct";
                case 10:
                    return "Nov";
                default:
                    return "Dec";
            }
        }
    }
}