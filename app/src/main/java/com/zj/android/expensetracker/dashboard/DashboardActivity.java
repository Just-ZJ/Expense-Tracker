package com.zj.android.expensetracker.dashboard;

import androidx.fragment.app.Fragment;

import com.zj.android.expensetracker.SingleFragmentActivity;

public class DashboardActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new DashboardFragment();
    }
}
