package com.zj.android.expensetracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zj.android.expensetracker.add_item.AddItemFragment;
import com.zj.android.expensetracker.dashboard.DashboardFragment;
import com.zj.android.expensetracker.transaction.TransactionFragment;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    private ViewPager2 mViewPager2;
    private FragmentStateAdapter mAdapter;

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        mViewPager2 = findViewById(R.id.fragment_container);
        mAdapter = new CustomFragmentStateAdapter(this);
        mViewPager2.setAdapter(mAdapter);

        // on click events for bottom app bar
        BottomNavigationItemView menu_dashboard = findViewById(R.id.bottom_appbar_dashboard);
        menu_dashboard.setOnClickListener(view -> {
            mViewPager2.setCurrentItem(0, true);
            // set color of icon when clicked
            bottomNavigationView.setSelectedItemId(R.id.bottom_appbar_dashboard);
        });
        FloatingActionButton menu_fab = findViewById(R.id.floating_action_button);
        menu_fab.setOnClickListener(view -> mViewPager2.setCurrentItem(1, true));
        BottomNavigationItemView menu_transaction = findViewById(R.id.bottom_appbar_transaction);
        menu_transaction.setOnClickListener(view -> {
            mViewPager2.setCurrentItem(2, true);
            // set color of icon when clicked
            bottomNavigationView.setSelectedItemId(R.id.bottom_appbar_transaction);
        });
    }

    public static class CustomFragmentStateAdapter extends FragmentStateAdapter {
        public CustomFragmentStateAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new DashboardFragment();
                case 1:
                    return new AddItemFragment();
                case 2:
                    return new TransactionFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            // number of fragments/pages
            return 3;
        }

    }
}
