package com.zj.android.expensetracker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zj.android.expensetracker.models.Expense;

/**
 * Used for passing data between fragments
 */
public class CustomViewModel extends ViewModel {
    private final MutableLiveData<Expense> newExpense = new MutableLiveData<>();

    public Expense getNewExpense() {
        return this.newExpense.getValue();
    }

    public void setNewExpense(Expense expense) {
        this.newExpense.setValue(expense);
    }
}
