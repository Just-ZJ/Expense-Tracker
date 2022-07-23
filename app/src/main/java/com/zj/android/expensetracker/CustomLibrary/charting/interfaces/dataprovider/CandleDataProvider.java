package com.zj.android.expensetracker.CustomLibrary.charting.interfaces.dataprovider;

import com.zj.android.expensetracker.CustomLibrary.charting.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
