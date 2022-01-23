package com.zj.android.expensetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

    private void populateChipGroup(){
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
//        updates mSelectedCategoriesTextView to show what is selected
        newChip.setOnClickListener(view -> {
            String text = (String) mSelectedCategoriesTextView.getText();
            if (!text.equals("")){
                text += ", " + category;
            }else{
                text += category;
            }
            mSelectedCategoriesTextView.setText(text);
        });
        return newChip;
    }
}