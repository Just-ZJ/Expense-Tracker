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
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.zj.android.expensetracker.CustomViewModel;
import com.zj.android.expensetracker.DatabaseAccessor;
import com.zj.android.expensetracker.R;
import com.zj.android.expensetracker.database.CategoryDataBase;
import com.zj.android.expensetracker.database.ExpenseDataBase;
import com.zj.android.expensetracker.database.ExpenseToCategoryDataBase;
import com.zj.android.expensetracker.models.Category;
import com.zj.android.expensetracker.models.Expense;
import com.zj.android.expensetracker.models.ExpenseToCategory;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class AddItemFragment extends Fragment {

    private final String[] DEFAULT_CATEGORIES = new String[]{"Grocery", "Fuel", "Dining",
            "Subscriptions", "Miscellaneous"};
    private TextView mDateTextView;
    private ImageView mSelectDateImageView;
    private TextView mSelectedCategoriesTextView;
    private ChipGroup mCategoriesChipGroup;
    private EditText mExpenseDetailsEditText;
    private EditText mAmountEditText;
    private Button mAddExpenseButton;
    private ExpenseDataBase mExpenseDataBase;
    private CategoryDataBase mCategoryDataBase;
    private ExpenseToCategoryDataBase mExpenseToCategoryDataBase;
    private View mView;
    private CustomViewModel mViewModel;
    private Calendar mCalendarSelectedDate;

    /*------------------------------ Helper Methods ------------------------------*/

    /**
     * Converts a number from 1-7, to a day in a week that it corresponds to
     *
     * @param num a number from 1-7
     * @return day of the week
     */
    public static String getDayString(int num) {
        switch (num) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
        }
        return null;
    }

    /**
     * Returns a string that is in the format of eg. Friday, 5/6/2022
     *
     * @param day   the day of the week
     * @param month the month
     * @param date  the date
     * @param year  the year
     * @return a string that is in the format of "Friday, 5/6/2022"
     */
    public static String formatDate(int day, int month, int date, int year) {
        return String.format(Locale.ENGLISH, "%s, %d/%d/%d",
                getDayString(day), month + 1, date, year);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);

        DatabaseAccessor databaseAccessor = new DatabaseAccessor(requireContext());
        mExpenseDataBase = new ExpenseDataBase(this.getContext());
        mCategoryDataBase = new CategoryDataBase(this.getContext());
        mExpenseToCategoryDataBase = new ExpenseToCategoryDataBase(this.getContext());

        mView = inflater.inflate(R.layout.activity_add_item, container, false);
        mDateTextView = mView.findViewById(R.id.textView_date);

        // set to today's date
        mCalendarSelectedDate = Calendar.getInstance(TimeZone.getDefault());
        try {
            mDateTextSetDate(mCalendarSelectedDate);
            // offset to ensure DatePicker is not shown as 1 day ahead
            mCalendarSelectedDate.add(Calendar.DATE, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSelectDateImageView = mView.findViewById(R.id.imageView_select_date);
        mSelectDateImageView.setOnClickListener(view -> createDatePicker());

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
            Expense expense = new Expense(mCalendarSelectedDate.getTimeInMillis(),
                    mSelectedCategoriesTextView.getText().toString(),
                    mExpenseDetailsEditText.getText().toString(),
                    Double.valueOf(mAmountEditText.getText().toString()));
            // add expense to databases
            mExpenseDataBase.addExpense(expense);
            String[] categories = expense.getCategories().split(", ");
            for (String s : categories) {
                Category category = DatabaseAccessor.getCategoryByName(s);
                ExpenseToCategory expenseToCategory = new ExpenseToCategory(
                        UUID.fromString(expense.getId().toString()), UUID.fromString(category.getId().toString()));
                mExpenseToCategoryDataBase.addCategory(expenseToCategory);
            }
            // store expense to view model
            mViewModel.setNewExpense(expense);

            // clear form
            clearFields();
            // switch to transaction fragment
            ViewPager2 viewPager2 = getActivity().findViewById(R.id.fragment_container);
            viewPager2.setCurrentItem(2, true);
        });
        return mView;
    }

    /**
     * Creates a DatePicker object for the user to select the date of their expense
     */
    private void createDatePicker() {
        // set to only being able to select dates before today's date
        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .build();

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(mCalendarSelectedDate.getTimeInMillis())
                .setCalendarConstraints(constraints)
                .build();

        // open and show the date picker fragment
        datePicker.show(requireActivity().getSupportFragmentManager(), null);
        // change date to what the user selected
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                mCalendarSelectedDate.setTimeInMillis(selection);
                try {
                    // offset to show correct date in text
                    mCalendarSelectedDate.add(Calendar.DATE, 1);
                    mDateTextSetDate(mCalendarSelectedDate);
                    mCalendarSelectedDate.add(Calendar.DATE, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * Sets the text of {@code mDateTextView}
     *
     * @param cal a calendar object
     */
    private void mDateTextSetDate(Calendar cal) {
        // offset date by 1 to show correct date in text
        mDateTextView.setText(formatDate(cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE), cal.get(Calendar.YEAR)));
    }

    /**
     * Clears all fields of the form and reset it to default
     */
    private void clearFields() {
        mSelectedCategoriesTextView.setText("");
        mExpenseDetailsEditText.setText("");
        mAmountEditText.setText("0.00");
        mCategoriesChipGroup.clearCheck();
    }

    /**
     * Updates {@code mSelectedCategoriesTextView} with what chip was selected by the user
     *
     * @param category the name of a category chip
     */
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
        // removes remaining spaces and commas at the start of string after deletion
        if (text.length() > 0) {
            while (text.charAt(0) == ',' || text.charAt(0) == ' ') {
                text.deleteCharAt(0);
            }
        }
        mSelectedCategoriesTextView.setText(text.toString());
    }

    /**
     * Adds the default categories to database if it does not already exist
     */
    private void addDefaultCategories() {
        for (String s : DEFAULT_CATEGORIES) {
            mCategoryDataBase.addCategory(new Category(s));
        }
    }

    /**
     * Populates chip group with categories in the database
     */
    private void populateChipGroup() {
        addDefaultCategories();
        String[] categories = DatabaseAccessor.getCategories();
        for (String s : categories) {
            mCategoriesChipGroup.addView(addChip(s, false));
        }
        mCategoriesChipGroup.addView(addChip("Add Category", true));
    }

    /**
     * Creates chips with category names
     *
     * @param category the category name
     * @param isAdd    indicates whether {@code category}  is "Add Category"
     * @return the chip created
     */
    private Chip addChip(String category, boolean isAdd) {
        LayoutInflater inflater = LayoutInflater.from(mView.getContext());
        Chip newChip = (Chip) inflater.inflate(R.layout.fragment_add_item_add_chip, this.mCategoriesChipGroup, false);
        newChip.setText(category);
        if (isAdd) {
            // customize "Add Category" chip
            newChip.setOnClickListener(view -> createAddChipDialog());
            newChip.setCheckable(false);
            newChip.setCloseIcon(getResources().getDrawable(R.drawable.ic_add));
        } else {
            // customize all other chips
            if (Arrays.asList(DEFAULT_CATEGORIES).contains(category)) {
                // customize default chips
                newChip.setCloseIconVisible(false);
            }
            newChip.setOnClickListener(view -> updateSelectedCategoriesTextView(category));
            newChip.setOnCloseIconClickListener(v -> removeChip(v, newChip.getText().toString()));
        }
        return newChip;
    }

    /**
     * Creates dialog for user to add category when the "Add Category" chip is clicked
     */
    private void createAddChipDialog() {
        // inflate view beforehand for setPositiveButton OnClickListener
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_categories, null);
        new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .setNeutralButton("Cancel", (dialogInterface, i) -> {
                    // do nothing
                })
                .setPositiveButton("Add Category", (dialogInterface, i) -> {
                    TextInputLayout textInputLayout = dialogView.findViewById(R.id.add_category_editText);
                    String inputText = textInputLayout.getEditText().getText().toString();
                    // add to chip group, just before "Add Category"
                    mCategoriesChipGroup.addView(addChip(inputText, false),
                            mCategoriesChipGroup.getChildCount() - 1);
                    // add to database
                    mCategoryDataBase.addCategory(new Category(inputText));
                })
                .show();
    }

    /**
     * Removes chip completely when it is deleted by the user
     */
    private void removeChip(View v, String categoryName) {
        // remove from chip group
        mCategoriesChipGroup.removeView(v);
        // remove from textview
        updateSelectedCategoriesTextView(categoryName);
        // remove from database
        DatabaseAccessor.removeCategory(categoryName);
    }
}