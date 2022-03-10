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
import java.util.List;

public class TransactionFragment extends Fragment {

    private ExpandableListView mExpandableListView;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_transaction, container, false);

        mExpandableListView = mView.findViewById(R.id.transaction_expandableListView);
        DatabaseAccessor databaseAccessor = new DatabaseAccessor(getContext());
        List<Expense> expenses = DatabaseAccessor.getExpenses();

        List<String> months = new ArrayList<>();
        months.add("Month 1");

        CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(mView.getContext(), expenses, months);
        mExpandableListView.setAdapter(adapter);
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                // update expenses
                List<Expense> expenses = DatabaseAccessor.getExpenses();
                adapter.updateItems(expenses);
            }
        });
        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {

            }
        });
        return mView;
    }

    public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
        Context context;
        List<Expense> expenses;
        List<String> months;

        public CustomExpandableListAdapter(Context context, List<Expense> expenses, List<String> months) {
            this.context = context;
            this.expenses = expenses;
            this.months = months;
        }

        public void updateItems(List<Expense> expenses) {
            this.expenses = expenses;
            notifyDataSetChanged();
        }

        @Override
        public int getGroupCount() {
            return months.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return expenses.size();
        }

        @Override
        public Object getGroup(int i) {
            return months.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            // getChild(int groupPosition, int childPosition)
            return expenses.get(i1);
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
            String totalAmount = "$100.00";
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
            String amount = expense.getAmount().toString();
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