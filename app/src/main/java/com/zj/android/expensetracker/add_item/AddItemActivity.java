package com.zj.android.expensetracker.add_item;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.zj.android.expensetracker.R;

import java.util.Calendar;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {

    private TextView mDateTextView;
    private ImageView mSelectDateImageView;
    private TextView mSelectedCategoriesTextView;
    private ChipGroup mCategoriesChipGroup;
    private EditText mExpenseDetails;
    private EditText mAmount;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mDateTextView = findViewById(R.id.textView_date);
        // shows today's date
        Calendar cal = Calendar.getInstance();
        mDateTextView.setText(formatDate(cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR)));

        mSelectDateImageView = findViewById(R.id.imageView_select_date);
        mSelectDateImageView.setOnClickListener(view -> {
            // TODO: open the date picker fragment
        });

        mSelectedCategoriesTextView = findViewById(R.id.textView_selected_categories);
        mCategoriesChipGroup = findViewById(R.id.chipGroup_expense_category);
        populateChipGroup();

        mExpenseDetails = findViewById(R.id.editText_expense_details);
        mExpenseDetails.addTextChangedListener(new TextWatcher() {
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

        mAmount = findViewById(R.id.editText_amount);
        mAmount.addTextChangedListener(new TextWatcher() {
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
    }

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
                // remove category
                text.delete(0, category.length());
                break;
            default:
                text.delete(pos - 2, pos + category.length());
        }
        if (text.length() > 0) while (text.charAt(0) == ',' || text.charAt(0) == ' ') {
            text.deleteCharAt(0);
        }
        mSelectedCategoriesTextView.setText(text.toString());
    }

    private void populateChipGroup() {
//        TODO: change this to create from an array of values
        mCategoriesChipGroup.addView(addChip("Grocery"));
        mCategoriesChipGroup.addView(addChip("Fuel"));
        mCategoriesChipGroup.addView(addChip("Dining"));
        mCategoriesChipGroup.addView(addChip("Subscriptions"));
        mCategoriesChipGroup.addView(addChip("Miscellaneous"));
    }

    private Chip addChip(String category) {
        LayoutInflater inflater = LayoutInflater.from(this);
        Chip newChip = (Chip) inflater.inflate(R.layout.fragment_add_chip, this.mCategoriesChipGroup, false);
        newChip.setText(category);
        // updates mSelectedCategoriesTextView to show what is selected
        newChip.setOnClickListener(view -> {
            updateSelectedCategoriesTextView(category);
        });
        return newChip;
    }


}