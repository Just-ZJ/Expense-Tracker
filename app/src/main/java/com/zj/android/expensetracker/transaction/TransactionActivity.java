package com.zj.android.expensetracker.transaction;

import androidx.fragment.app.Fragment;

import com.zj.android.expensetracker.SingleFragmentActivity;
import com.zj.android.expensetracker.dashboard.DashboardFragment;

public class TransactionActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new TransactionFragment();
    }
}
