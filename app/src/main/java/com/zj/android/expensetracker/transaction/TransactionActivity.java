package com.zj.android.expensetracker.transaction;

import androidx.fragment.app.Fragment;

import com.zj.android.expensetracker.SingleFragmentActivity;

public class TransactionActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new TransactionFragment();
    }
}
