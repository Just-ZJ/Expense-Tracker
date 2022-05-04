package com.zj.android.expensetracker.add_item;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.zj.android.expensetracker.CustomViewModel;
import com.zj.android.expensetracker.R;
import com.zj.android.expensetracker.database.ExpenseDataBase;
import com.zj.android.expensetracker.models.Expense;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddItemFragment extends Fragment {

    private TextView mDateTextView;
    private ImageView mSelectDateImageView;
    private TextView mSelectedCategoriesTextView;
    private ChipGroup mCategoriesChipGroup;
    private EditText mExpenseDetailsEditText;
    private EditText mAmountEditText;
    private Button mAddExpenseButton;
    private ExpenseDataBase mDataBase;

    private View mView;
    private CustomViewModel mViewModel;

    private String getDayString(int num) {
        String day;
        switch (num) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;
            default:
                day = "Error: Day does not exist";
        }
        return day;
    }

    private String formatDate(int day, int month, int date, int year) {
        return String.format(Locale.ENGLISH, "%s, %d/%d/%d",
                getDayString(day), month + 1, date, year);
    }

    private void mDateTextSetDate(Calendar cal) {
        mDateTextView.setText(formatDate(cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR)));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
        mDataBase = new ExpenseDataBase(this.getContext());

        mView = inflater.inflate(R.layout.activity_add_item, container, false);
        mDateTextView = mView.findViewById(R.id.textView_date);

        // set to today's date
        Calendar cal = Calendar.getInstance();
        mDateTextSetDate(cal);

        mSelectDateImageView = mView.findViewById(R.id.imageView_select_date);
        mSelectDateImageView.setOnClickListener(view -> {
            // set to only being able to select dates before today's date
            CalendarConstraints constraints = new CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now())
                    .build();

            MaterialDatePicker<?> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date")
                    .setSelection(cal.getTimeInMillis())
                    .setCalendarConstraints(constraints)
                    .build();

            // open and show the date picker fragment
            datePicker.show(getActivity().getSupportFragmentManager(), null);

            datePicker.addOnPositiveButtonClickListener(listener -> {
                try {
                    // set to selected date
                    long dateVal = Long.parseLong(datePicker.getSelection().toString());
                    cal.setTime(new Date(dateVal));
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Invalid Date from MaterialDatePicker.");
                }
                // offset by 1 day to show date correctly in text
                cal.add(Calendar.DATE, 1);
                mDateTextSetDate(cal);
                cal.add(Calendar.DATE, -1);
            });
        });

        mSelectedCategoriesTextView = mView.findViewById(R.id.textView_selected_categories);
        mCategoriesChipGroup = mView.findViewById(R.id.chipGroup_expense_category);
        populateChipGroup();

        mExpenseDetailsEditText = mView.findViewById(R.id.editText_expense_details);
        mExpenseDetailsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAmountEditText = mView.findViewById(R.id.editText_amount);
        mAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mAddExpenseButton = mView.findViewById(R.id.add_expense_button);
        mAddExpenseButton.setOnClickListener(view -> {
            // add expense to database
            Double amount = Double.valueOf(mAmountEditText.getText().toString());
            Expense expense = new Expense();
            expense.setDate(mDateTextView.getText().toString());
            expense.setCategories(mSelectedCategoriesTextView.getText().toString());
            expense.setDetails(mExpenseDetailsEditText.getText().toString());
            expense.setAmount(amount);
            mDataBase.addExpense(expense);
            // store expense to viewmodel
            mViewModel.setNewExpense(expense);
            // clear form
            clearFields();
            // switch to transaction fragment
            ViewPager2 viewPager2 = getActivity().findViewById(R.id.fragment_container);
            viewPager2.setCurrentItem(2, true);
        });
        return mView;
    }


    private void clearFields() {
        mSelectedCategoriesTextView.setText("");
        mExpenseDetailsEditText.setText("");
        mAmountEditText.setText("0.00");
    }

    // update text view with what chip was selected
    private void updateSelectedCategoriesTextView(String category) {
        StringBuilder text = new StringBuilder((String) mSelectedCategoriesTextView.getText());
        int pos = text.indexOf(category);
        switch (pos) {
            case -1:
                // add category
                if (text.length() != 0) text.append(", ");
                text.append(category);
                break;
            case 0:
                // remove category for index 0
                text.delete(0, category.length());
                break;
            default:
                // remove category for non index 0
                text.delete(pos - 2, pos + category.length());
        }
        if (text.length() > 0) while (text.charAt(0) == ',' || text.charAt(0) == ' ') {
            text.deleteCharAt(0);
        }
        mSelectedCategoriesTextView.setText(text.toString());
    }

    private void populateChipGroup() {
        //        TODO: change this to create from an array of values
        //        TODO: change this to using database
        mCategoriesChipGroup.addView(addChip("Grocery"));
        mCategoriesChipGroup.addView(addChip("Fuel"));
        mCategoriesChipGroup.addView(addChip("Dining"));
        mCategoriesChipGroup.addView(addChip("Subscriptions"));
        mCategoriesChipGroup.addView(addChip("Miscellaneous"));
    }

    private Chip addChip(String category) {
        LayoutInflater inflater = LayoutInflater.from(mView.getContext());
        Chip newChip = (Chip) inflater.inflate(R.layout.fragment_add_item_add_chip, this.mCategoriesChipGroup, false);
        newChip.setText(category);
        // updates mSelectedCategoriesTextView to show what is selected
        newChip.setOnClickListener(view -> updateSelectedCategoriesTextView(category));
        return newChip;
    }
}