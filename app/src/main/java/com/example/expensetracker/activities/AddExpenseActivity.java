package com.example.expensetracker.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.database.ExpenseDbHelper;
import com.example.expensetracker.model.Expense;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private TextView tvDate;
    private long selectedDate;
    private Button btnSaveExpense;
    private Button btnDeleteExpense;
    private EditText etTitle;
    private EditText etAmount;
    private ExpenseDbHelper dbHelper;

    private boolean isEditMode = false;
    private int expenseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_expense);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        tvDate = findViewById(R.id.tvDate);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);
        btnDeleteExpense = findViewById(R.id.btnDeleteExpense);
        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);

        btnDeleteExpense.setVisibility(View.GONE);

        dbHelper = new ExpenseDbHelper(this);

        String[] categories = {"Food", "Transport", "Bills", "Entertainment", "Shopping", "Health", "Education", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);

        spinnerCategory.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();

        selectedDate = calendar.getTimeInMillis();

        checkEditMode();

        updateDateText();

        tvDate.setOnClickListener(v -> {

            calendar.setTimeInMillis(selectedDate);

            DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {

                Calendar selected = Calendar.getInstance();

                selected.set(year, month, dayOfMonth);

                selectedDate = selected.getTimeInMillis();

                updateDateText();

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            dialog.show();

        });

        btnSaveExpense.setOnClickListener(v -> saveExpense());

        btnDeleteExpense.setOnClickListener(v -> deleteExpense());

    }

    private void checkEditMode() {

        if (getIntent().hasExtra("id")) {

            isEditMode = true;
            expenseId = getIntent().getIntExtra("id", -1);
            String title = getIntent().getStringExtra("title");
            double amount = getIntent().getDoubleExtra("amount", 0);
            String category = getIntent().getStringExtra("category");
            selectedDate = getIntent().getLongExtra("date", selectedDate);

            etTitle.setText(title);
            etAmount.setText(String.valueOf(amount));

            int position = ((ArrayAdapter<String>) spinnerCategory.getAdapter()).getPosition(category);
            spinnerCategory.setSelection(position);

            btnSaveExpense.setText("Update Expense");
            btnDeleteExpense.setVisibility(View.VISIBLE);

        }

    }

    private void updateDateText() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(selectedDate));

    }

    private void saveExpense() {

        String title = etTitle.getText().toString().trim();
        String amountText = etAmount.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter expense title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amountText.isEmpty()) {
            Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        Expense expense;

        if (isEditMode) {
            expense = new Expense(expenseId, title, amount, category, selectedDate);
            int rows = dbHelper.updateExpense(expense);
            if (rows > 0) {
                Toast.makeText(this, "Expense updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            expense = new Expense(title, amount, category, selectedDate);
            long id = dbHelper.insertExpense(expense);
            if (id != -1) {
                Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    private void deleteExpense() {

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_delete_expense, null);


        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();


        Button btnCancel = dialogView.findViewById(R.id.btnCancelDelete);
        Button btnDelete = dialogView.findViewById(R.id.btnConfirmDelete);


        btnCancel.setOnClickListener(v -> dialog.dismiss());


        btnDelete.setOnClickListener(v -> {

            int rows = dbHelper.deleteExpense(expenseId);


            if (rows > 0) {

                Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                finish();

            }

        });


        dialog.show();
    }
}