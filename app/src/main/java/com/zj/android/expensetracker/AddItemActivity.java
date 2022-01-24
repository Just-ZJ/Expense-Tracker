package com.zj.android.expensetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class AddItemActivity extends AppCompatActivity {

    private TextView mDateTextView;
    private ImageView mSelectDateImageView;
    private TextView mSelectedCategoriesTextView;
    private ChipGroup mCategoriesChipGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mDateTextView = findViewById(R.id.textView_date);
        mSelectDateImageView = findViewById(R.id.imageView_select_date);
        mSelectDateImageView.setOnClickListener(view -> {
            // TODO: open the datepicker fragment
        });
        mCategoriesChipGroup = findViewById(R.id.chipGroup_expense_category);
        populateChipGroup();
        mSelectedCategoriesTextView = findViewById(R.id.textView_selected_categories);


    }

    private void updateSelectedCategoriesTextView(String category) {
        StringBuilder text = new StringBuilder((String) mSelectedCategoriesTextView.getText());
        int pos = text.indexOf(category);
        switch (pos){
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
        if(text.length() > 0) while(text.charAt(0) == ',' || text.charAt(0) == ' '){
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
        Chip newChip = (Chip) inflater.inflate(R.layout.action_add_chip, this.mCategoriesChipGroup, false);
        newChip.setText(category);
        // updates mSelectedCategoriesTextView to show what is selected
        newChip.setOnClickListener(view -> {
            updateSelectedCategoriesTextView(category);
        });
        return newChip;
    }
}