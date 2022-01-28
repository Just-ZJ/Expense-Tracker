package com.zj.android.expensetracker.dashboard;

import androidx.fragment.app.Fragment;

import com.zj.android.expensetracker.SingleFragmentActivity;
import com.zj.android.expensetracker.add_item.AddItemFragment;

public class DashboardActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new DashboardFragment();
    }
}
