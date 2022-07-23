package com.zj.android.expensetracker.CustomLibrary.charting.interfaces.dataprovider;

import com.zj.android.expensetracker.CustomLibrary.charting.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
