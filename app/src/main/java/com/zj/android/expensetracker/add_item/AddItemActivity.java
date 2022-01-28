package com.zj.android.expensetracker.add_item;

import androidx.fragment.app.Fragment;

import com.zj.android.expensetracker.SingleFragmentActivity;

public class AddItemActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AddItemFragment();
    }
}
