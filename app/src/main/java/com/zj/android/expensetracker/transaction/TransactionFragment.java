package com.zj.android.expensetracker.transaction;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zj.android.expensetracker.CustomDate;
import com.zj.android.expensetracker.R;
import com.zj.android.expensetracker.database.DatabaseAccessor;
import com.zj.android.expensetracker.models.Expense;

import java.util.Calendar;
import java.util.List;

public class TransactionFragment extends Fragment {

    private ExpandableListView mExpandableListView;
    private View mView;
    private CustomExpandableListAdapter mCustomExpandableListAdapter;
//    private CustomViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_transaction, container, false);
//        mViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);

        mExpandableListView = mView.findViewById(R.id.transaction_expandableListView);
        DatabaseAccessor databaseAccessor = new DatabaseAccessor(requireContext());

        mCustomExpandableListAdapter = new CustomExpandableListAdapter(mView.getContext());
        mExpandableListView.setAdapter(mCustomExpandableListAdapter);

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                // update expenses
                mCustomExpandableListAdapter.updateItems();
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
        mCustomExpandableListAdapter.updateItems();
    }

    /*------------------------------ Helper Methods ------------------------------*/
    public void collapseAll() {
        for (int i = 0; i < mExpandableListView.getExpandableListAdapter().getGroupCount(); i++) {
            mExpandableListView.collapseGroup(i);
        }
    }

    public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
        Context mContext;
        List<String> mMonths;

        public CustomExpandableListAdapter(Context context) {
            this.mContext = context;
            this.mMonths = DatabaseAccessor.getUniqueMonthYear();
        }

        @Override
        public int getGroupCount() {
            return mMonths.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return DatabaseAccessor.getMonthYearCount(getGroup(i));
        }

        /**
         * Gets the heading for the group
         * Ex: May 2022
         */
        @Override
        public String getGroup(int i) {
            try {
                return mMonths.get(i);
            } catch (IndexOutOfBoundsException e) {
                return "Empty Group";
            }
        }

        @Override
        public Object getChild(int i, int i1) {
            // getChild(int groupPosition, int childPosition)
            return DatabaseAccessor.getExpenses(getGroup(i)).get(i1);
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
            String month = getGroup(i);
            if (month.equals("Empty Group")) {
                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                return inflater.inflate(R.layout.fragment_transaction_empty, null);
            }
            String totalAmount = String.format("$%.2f", DatabaseAccessor.getExpenseAmount(month));
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.fragment_transaction_expandable_groups, null);
            }
            TextView groupMonth = view.findViewById(R.id.transaction_group_month);
            groupMonth.setText(convertGroupViewText(month));
            TextView groupTotalAmt = view.findViewById(R.id.transaction_group_amount);
            groupTotalAmt.setText(totalAmount);
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            Expense expense = (Expense) getChild(i, i1);
            if (expense == null) {
                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                return inflater.inflate(R.layout.fragment_transaction_empty, null);
            }
            Calendar cal = expense.getDate().getCalendar();
            String amount = String.format("$%.2f", expense.getAmount());
            String details = expense.getDetails();
            String categories = expense.getCategory();
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.fragment_transaction_expandable_item, null);
            }
            TextView transactionDate = view.findViewById(R.id.transaction_item_date);
            try {
                transactionDate.setText(new CustomDate(cal).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            TextView transactionAmount = view.findViewById(R.id.transaction_item_amount);
            transactionAmount.setText(amount);
            TextView transactionCategories = view.findViewById(R.id.transaction_item_category);
            transactionCategories.setText(categories);
            TextView transactionDetails = view.findViewById(R.id.transaction_item_details);
            transactionDetails.setText(details);

            ImageButton deleteButton = view.findViewById(R.id.button_delete_transaction);
            deleteButton.setOnClickListener(v -> new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Are you sure you want to delete this expense?")
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // do nothing
                        }
                    }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // delete expense
                            DatabaseAccessor.removeExpense(expense);
                            updateItems();
                        }
                    }).show());

            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

        /****************** Helper Methods for CustomExpandableListAdapter ******************/

        public void updateItems() {
            this.mMonths = DatabaseAccessor.getUniqueMonthYear();
            notifyDataSetChanged();
        }

        /**
         * Converts @period from '2022-07'  to 'July 2022'
         *
         * @param period a string in the form of '2022-07'
         * @return returns a string in the form of 'July 2022'
         */
        public String convertGroupViewText(String period) {
            String[] tmp = period.split("-");
            String result;
            switch (tmp[1]) {
                case "01":
                    result = "January";
                    break;
                case "02":
                    result = "February";
                    break;
                case "03":
                    result = "March";
                    break;
                case "04":
                    result = "April";
                    break;
                case "05":
                    result = "May";
                    break;
                case "06":
                    result = "June";
                    break;
                case "07":
                    result = "July";
                    break;
                case "08":
                    result = "August";
                    break;
                case "09":
                    result = "September";
                    break;
                case "10":
                    result = "October";
                    break;
                case "11":
                    result = "November";
                    break;
                default:
                    result = "December";
                    break;
            }
            return result + " " + tmp[0];
        }
    }


}