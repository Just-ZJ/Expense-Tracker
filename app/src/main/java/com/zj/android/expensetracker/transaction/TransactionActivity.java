package com.zj.android.expensetracker.transaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zj.android.expensetracker.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {
    ExpandableListView mExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        mExpandableListView = findViewById(R.id.transaction_expandableListView);
        List<String> transactions = new ArrayList<>();
        List<String> months = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            if (i < 13) months.add("Month" + i);
            transactions.add(i + " January 2022");
        }
        mExpandableListView.setAdapter(new CustomExpandableListAdapter(this, transactions, months));
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

            }
        });
        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {

            }
        });
    }

    public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
        Context context;
        List<String> transactions;
        List<String> months;

        public CustomExpandableListAdapter(Context context, List<String> transactions, List<String> months) {
            this.context = context;
            this.transactions = transactions;
            this.months = months;
        }

        @Override
        public int getGroupCount() {
            return months.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return transactions.size();
        }

        @Override
        public Object getGroup(int i) {
            return months.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return transactions.get(i);
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
                view = inflater.inflate(R.layout.fragment_expandable_groups, null);
            }
            TextView groupMonth = view.findViewById(R.id.transaction_group_month);
            groupMonth.setText(month);
            TextView groupTotalAmt = view.findViewById(R.id.transaction_group_amount);
            groupTotalAmt.setText(totalAmount);
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            String date = (String) getChild(i, i1);
            String amount = "$0.00";
            String details = "Details of the transaction is .......";
            String categories = "ALL CATEGORIES";
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.fragment_expandable_item, null);
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