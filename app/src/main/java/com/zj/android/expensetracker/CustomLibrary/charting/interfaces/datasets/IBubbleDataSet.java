package com.zj.android.expensetracker.CustomLibrary.charting.interfaces.datasets;

import com.zj.android.expensetracker.CustomLibrary.charting.data.BubbleEntry;

/**
 * Created by philipp on 21/10/15.
 */
public interface IBubbleDataSet extends IBarLineScatterCandleBubbleDataSet<BubbleEntry> {

    float getMaxSize();

    boolean isNormalizeSizeEnabled();

    /**
     * Returns the width of the highlight-circle that surrounds the bubble
     *
     * @return
     */
    float getHighlightCircleWidth();

    /**
     * Sets the width of the circle that surrounds the bubble when highlighted,
     * in dp.
     *
     * @param width
     */
    void setHighlightCircleWidth(float width);
}