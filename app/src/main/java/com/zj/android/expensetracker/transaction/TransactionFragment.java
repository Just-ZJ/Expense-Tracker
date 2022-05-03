package com.zj.android.expensetracker.transaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zj.android.expensetracker.DatabaseAccessor;
import com.zj.android.expensetracker.R;
import com.zj.android.expensetracker.models.Expense;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TransactionFragment extends Fragment {

    private ExpandableListView mExpandableListView;
    private View mView;
    private CustomExpandableListAdapter mCustomExpandableListAdapter;

    private String getMonthString(int num) {
        String month;
        switch (num) {
            case 0:
                month = "January";
                break;
            case 1:
                month = "February";
                break;
            case 2:
                month = "March";
                break;
            case 3:
                month = "April";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "June";
                break;
            case 6:
                month = "July";
                break;
            case 7:
                month = "August";
                break;
            case 8:
                month = "September";
                break;
            case 9:
                month = "October";
                break;
            case 10:
                month = "November";
                break;
            case 11:
                month = "December";
                break;
            default:
                month = "Error: Day does not exist";
        }
        return month;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_transaction, container, false);

        mExpandableListView = mView.findViewById(R.id.transaction_expandableListView);
        DatabaseAccessor databaseAccessor = new DatabaseAccessor(getContext());
        List<Expense> expenses = DatabaseAccessor.getExpenses();

        List<String> months = new ArrayList<>();
        for (Expense e : expenses) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(e.getDate()));
            String month = getMonthString(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR);
            if (!months.contains(month)) {
                months.add(month);
            }
        }

        mCustomExpandableListAdapter = new CustomExpandableListAdapter(mView.getContext(), expenses, months);
        mExpandableListView.setAdapter(mCustomExpandableListAdapter);

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                // update expenses
                List<Expense> expenses = DatabaseAccessor.getExpenses();
                mCustomExpandableListAdapter.updateItems(expenses);
            }
        });
        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {

            }
        });
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // close all expandable lists so that it can refresh
        collapseAll();
    }

    private void collapseAll() {
        for (int i = 0; i < mExpandableListView.getExpandableListAdapter().getGroupCount(); i++) {
            mExpandableListView.collapseGroup(i);
        }
    }

    public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
        Context context;
        HashMap<String, List<Expense>> expenses;
        List<String> months;

        public CustomExpandableListAdapter(Context context, List<Expense> expenses, List<String> months) {
            this.context = context;
            this.months = months;
            this.expenses = new HashMap<>();
            createMap(expenses, months);
        }

        public void createMap(List<Expense> expenses, List<String> months) {
            for (String s : months) {
                this.expenses.put(s, new ArrayList<>());
            }
            for (Expense e : expenses) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(e.getDate()));
                String month = getMonthString(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR);
                List<Expense> tmp = this.expenses.get(month);
                tmp.add(e);
            }
        }

        public void updateItems(List<Expense> expenses) {
            createMap(expenses, months);
            notifyDataSetChanged();
        }

        @Override
        public int getGroupCount() {
            return months.size();
        }

        @Override
        public int getChildrenCount(int i) {
            // 	getChildrenCount(int groupPosition)
            return expenses.get(months.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return months.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            // getChild(int groupPosition, int childPosition)
            return expenses.get(months.get(i)).get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            String month = (String) getGroup(i);
            double amount = 0.00;
            List<Expense> currExpenses = expenses.get(months.get(i));
            for (Expense e : currExpenses) {
                amount += e.getAmount();
            }
            String totalAmount = String.format("$%.2f", amount);

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.fragment_transaction_expandable_groups, null);
            }
            TextView groupMonth = view.findViewById(R.id.transaction_group_month);
            groupMonth.setText(month);
            TextView groupTotalAmt = view.findViewById(R.id.transaction_group_amount);
            groupTotalAmt.setText(totalAmount);
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            Expense expense = (Expense) getChild(i, i1);
            String date = expense.getDate();
            String amount = String.format("$%.2f", expense.getAmount());
            String details = expense.getDetails();
            String categories = expense.getCategories();
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.fragment_transaction_expandable_item, null);
            }
            TextView transactionDate = view.findViewById(R.id.transaction_item_date);
            transactionDate.setText(date);
            TextView transactionAmount = view.findViewById(R.id.transaction_item_amount);
            transactionAmount.setText(amount);
            TextView transactionCategories = view.findViewById(R.id.transaction_item_category);
            transactionCategories.setText(categories);
            TextView transactionDetails = view.findViewById(R.id.transaction_item_details);
            transactionDetails.setText(details);

            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }
    }
}