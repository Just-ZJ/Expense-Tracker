package com.zj.android.expensetracker.add_item;

import androidx.fragment.app.Fragment;

import com.zj.android.expensetracker.SingleFragmentActivity;

public class AddItemActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new AddItemFragment();
    }
}
