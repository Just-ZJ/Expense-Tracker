package com.zj.android.expensetracker.CustomLibrary.charting.interfaces.dataprovider;

import com.zj.android.expensetracker.CustomLibrary.charting.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BarData getBarData();

    boolean isDrawBarShadowEnabled();

    boolean isDrawValueAboveBarEnabled();

    boolean isHighlightFullBarEnabled();
}
