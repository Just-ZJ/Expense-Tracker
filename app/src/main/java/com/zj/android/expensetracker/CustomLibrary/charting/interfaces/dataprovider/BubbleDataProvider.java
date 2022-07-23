package com.zj.android.expensetracker.CustomLibrary.charting.interfaces.dataprovider;

import com.zj.android.expensetracker.CustomLibrary.charting.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
