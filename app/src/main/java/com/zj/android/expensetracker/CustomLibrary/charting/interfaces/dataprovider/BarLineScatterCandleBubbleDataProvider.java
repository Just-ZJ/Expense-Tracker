package com.zj.android.expensetracker.CustomLibrary.charting.interfaces.dataprovider;

import com.zj.android.expensetracker.CustomLibrary.charting.components.YAxis.AxisDependency;
import com.zj.android.expensetracker.CustomLibrary.charting.data.BarLineScatterCandleBubbleData;
import com.zj.android.expensetracker.CustomLibrary.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);

    boolean isInverted(AxisDependency axis);

    float getLowestVisibleX();

    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
