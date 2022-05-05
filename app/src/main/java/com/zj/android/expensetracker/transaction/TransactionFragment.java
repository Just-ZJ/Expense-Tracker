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
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zj.android.expensetracker.CustomViewModel;
import com.zj.android.expensetracker.DatabaseAccessor;
import com.zj.android.expensetracker.R;
import com.zj.android.expensetracker.models.Expense;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TransactionFragment extends Fragment {

    private ExpandableListView mExpandableListView;
    private View mView;
    private CustomExpandableListAdapter mCustomExpandableListAdapter;
    private CustomViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_transaction, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);

        mExpandableListView = mView.findViewById(R.id.transaction_expandableListView);
        DatabaseAccessor databaseAccessor = new DatabaseAccessor(getContext());
        List<Expense> expenses = DatabaseAccessor.getExpenses();

        mCustomExpandableListAdapter = new CustomExpandableListAdapter(mView.getContext(), expenses);
        mExpandableListView.setAdapter(mCustomExpandableListAdapter);

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                // update expenses
                List<Expense> expenses = DatabaseAccessor.getExpenses();
                mCustomExpandableListAdapter.updateItems();
            }
        });
        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                mCustomExpandableListAdapter.updateItems();
            }
        });
//        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Log.i("result2", "groupPosition: " + groupPosition + ", childPosition: " + childPosition
//                        + ", " + id);
//
//                Button deleteButton = v.findViewById(R.id.button_delete_transaction);
//                if (deleteButton.getVisibility() == View.VISIBLE) {
//                    deleteButton.setVisibility(View.INVISIBLE);
//                } else {
//                    deleteButton.setVisibility(View.VISIBLE);
//                }
//
////                TextView amountTextView = v.findViewById(R.id.transaction_item_amount);
////                if (amountTextView.getVisibility() == View.VISIBLE) {
////                    amountTextView.setVisibility(View.INVISIBLE);
////                } else {
////                    amountTextView.setVisibility(View.VISIBLE);
////                }
//                return true;
//            }
//        });
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // close all expandable lists so that it can refresh
        collapseAll();
    }

    /******************************* Helper Methods *******************************/
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

    private int getMonthInt(String month) {
        switch (month) {
            case "January":
                return 0;
            case "February":
                return 1;
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "August":
                return 7;
            case "September":
                return 8;
            case "October":
                return 9;
            case "November":
                return 10;
            case "December":
            default:
                return 11;
        }
    }

    public void collapseAll() {
        for (int i = 0; i < mExpandableListView.getExpandableListAdapter().getGroupCount(); i++) {
            mExpandableListView.collapseGroup(i);
        }
    }

    public class ExpenseComparator implements Comparator<Expense> {
        @Override
        public int compare(Expense o1, Expense o2) {
            return 0;
        }
    }

    public class MonthYearComparator implements Comparator<String> {

        @Override
        public int compare(String o2, String o1) {
            String[] s1 = o1.split(" ");
            String[] s2 = o2.split(" ");
            int result = compareYear(s1[1], s2[1]);
            if (result == 0) {
                // only need to compare month if they are in the same year
                result = compareMonth(s1[0], s2[0]);
            }
            return result;
        }

        private int compareMonth(String m1, String m2) {
            int month1 = getMonthInt(m1);
            int month2 = getMonthInt(m2);
            if (month1 < month2) {
                return -1;
            } else if (month1 > month2) {
                return 1;
            }
            return 0;
        }

        private int compareYear(String y1, String y2) {
            try {
                int year1 = Integer.parseInt(y1);
                int year2 = Integer.parseInt(y2);
                if (year1 < year2) {
                    return -1;
                } else if (year1 == year2) {
                    return 0;
                } else {
                    return 1;
                }
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid INT to be converted to Year.");
            }
        }
    }

    public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
        Context mContext;
        HashMap<String, List<Expense>> mExpenses;
        List<String> mMonths;
        HashMap<String, Double> mMonthsAmount;

        public CustomExpandableListAdapter(Context context, List<Expense> expenses) {
            this.mContext = context;
            this.mExpenses = new HashMap<>();
            this.mMonths = new ArrayList<>();
            this.mMonthsAmount = new HashMap<>();
            createMap(expenses);
        }

        @Override
        public int getGroupCount() {
            return mExpenses.size();
        }

        @Override
        public int getChildrenCount(int i) {
            // 	getChildrenCount(int groupPosition)
            return mExpenses.get(mMonths.get(i)).size();
        }

        /**
         * Gets the heading for the group
         * Ex: May 2022
         */
        @Override
        public Object getGroup(int i) {
            return mMonths.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            // getChild(int groupPosition, int childPosition)
            return mExpenses.get(mMonths.get(i)).get(i1);
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
            String totalAmount = String.format("$%.2f", mMonthsAmount.get(month));
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            ImageButton deleteButton = view.findViewById(R.id.button_delete_transaction);
            deleteButton.setOnClickListener(v -> new MaterialAlertDialogBuilder(getContext())
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
                            removeFromExpenses(expense);
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
        public void createMap(List<Expense> expenses) {
            for (Expense e : expenses) {
                addToExpenses(e);
            }
        }

        private void removeFromExpenses(Expense expense) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(expense.getDate()));
            String month = getMonthString(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR);
            //remove from expenses
            List<Expense> tmp = mExpenses.get(month);
            tmp.remove(expense);
            if (tmp.size() > 0) {
                // transactions still exists for this group
                // decrement by expense amount
                double amount = mMonthsAmount.get(month) - expense.getAmount();
                mMonthsAmount.remove(month);
                mMonthsAmount.put(month, amount);
            } else {
                // no more transactions for this group
                mMonthsAmount.remove(month);
                mMonths.remove(month);
            }
        }

        private void addToExpenses(Expense expense) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(expense.getDate()));
            String month = getMonthString(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR);
            List<Expense> tmp = mExpenses.get(month);
            // add new key pair
            if (tmp == null) {
                mExpenses.put(month, new ArrayList<>());
                tmp = mExpenses.get(month);
                mMonths.add(month);
                mMonthsAmount.put(month, 0.0);
            }
            tmp.add(expense);
            // increment by expense amount
            double amount = mMonthsAmount.get(month) + expense.getAmount();
            mMonthsAmount.remove(month);
            mMonthsAmount.put(month, amount);
            // sort according to
            Collections.sort(mMonths, new MonthYearComparator());
        }

        public void updateItems() {
            Expense expense = mViewModel.getNewExpense();
            if (expense != null) {
                addToExpenses(expense);
                // clear so that it would not be added again to transactions
                mViewModel.setNewExpense(null);
            }
            notifyDataSetChanged();
        }
    }


}