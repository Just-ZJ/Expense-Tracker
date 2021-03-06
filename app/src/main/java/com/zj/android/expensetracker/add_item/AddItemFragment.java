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
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.zj.android.expensetracker.CustomDate;
import com.zj.android.expensetracker.R;
import com.zj.android.expensetracker.database.DatabaseAccessor;
import com.zj.android.expensetracker.database.ExpenseDataBase;
import com.zj.android.expensetracker.models.Category;
import com.zj.android.expensetracker.models.Expense;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class AddItemFragment extends Fragment {

    private final String[] DEFAULT_CATEGORIES = new String[]{"Salary", "Grocery", "Fuel", "Dining",
            "Subscriptions", "Miscellaneous"};
    private TextView mDateTextView;
    private ImageView mSelectDateImageView;
    private String mSelectedCategory;
    private ChipGroup mCategoriesChipGroup;
    private EditText mExpenseDetailsEditText;
    private EditText mAmountEditText;
    private Button mAddExpenseButton;
    private ExpenseDataBase mExpenseDataBase;

    private View mView;
    //    private CustomViewModel mViewModel;
    private Calendar mCalendarSelectedDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);

        DatabaseAccessor databaseAccessor = new DatabaseAccessor(requireContext());
        mExpenseDataBase = new ExpenseDataBase(this.getContext());
//        mExpenseDataBase.databaseSeeder();

        mView = inflater.inflate(R.layout.activity_add_item, container, false);
        mDateTextView = mView.findViewById(R.id.textView_date);

        // set to today's date
        mCalendarSelectedDate = Calendar.getInstance(TimeZone.getDefault());
        try {
            mDateTextSetDate(mCalendarSelectedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSelectDateImageView = mView.findViewById(R.id.imageView_select_date);
        mSelectDateImageView.setOnClickListener(view -> createDatePicker());

        mCategoriesChipGroup = mView.findViewById(R.id.chipGroup_expense_category);
        mCategoriesChipGroup.setSingleSelection(true);
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
            Expense expense = new Expense(new CustomDate(mCalendarSelectedDate),
                    mSelectedCategory,
                    mExpenseDetailsEditText.getText().toString(),
                    Double.valueOf(mAmountEditText.getText().toString()));
            // add expense to databases
            mExpenseDataBase.addExpense(expense);

            // store expense to view model
//            mViewModel.setNewExpense(expense);

            // clear form
            clearFields();
            // switch to transaction fragment
            ViewPager2 viewPager2 = getActivity().findViewById(R.id.fragment_container);
            viewPager2.setCurrentItem(2, true);
        });
        return mView;
    }

    /*------------------------------ Helper Methods ------------------------------*/

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
                .setSelection(Calendar.getInstance().getTimeInMillis())
                .setCalendarConstraints(constraints)
                .build();

        // open and show the date picker fragment
        datePicker.show(requireActivity().getSupportFragmentManager(), null);
        // change date to what the user selected
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                mCalendarSelectedDate.setTimeInMillis(selection);
                //offset to store correct date
                mCalendarSelectedDate.add(Calendar.DATE, 1);
                try {
                    // offset to show correct date in text
                    mDateTextSetDate(mCalendarSelectedDate);
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
        mDateTextView.setText(new CustomDate(cal).toString());
    }

    /**
     * Clears all fields of the form and reset it to default
     */
    private void clearFields() {
        mSelectedCategory = "";
        mExpenseDetailsEditText.setText("");
        mAmountEditText.setText("0.00");
        mCategoriesChipGroup.clearCheck();
    }

    /**
     * Adds the default categories to database if it does not already exist
     */
    private void addDefaultCategories() {
        for (String s : DEFAULT_CATEGORIES) {
            mExpenseDataBase.addCategory(new Category(s));
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
            //TODO: make it so that only can select 1 category
            newChip.setOnClickListener(view -> {
                mCategoriesChipGroup.check(view.getId());
                mSelectedCategory = newChip.getText().toString();
            });
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
                    mExpenseDataBase.addCategory(new Category(inputText));
                })
                .show();
    }

    /**
     * Removes chip completely when it is deleted by the user
     */
    private void removeChip(View v, String categoryName) {
        // remove from chip group
        mCategoriesChipGroup.removeView(v);
        // remove from database
        DatabaseAccessor.removeCategory(categoryName);
    }
}