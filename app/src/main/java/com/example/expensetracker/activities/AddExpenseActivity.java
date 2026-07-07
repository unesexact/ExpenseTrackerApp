package com.example.expensetracker.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
    private EditText etTitle;
    private EditText etAmount;
    private ExpenseDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_expense);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        tvDate = findViewById(R.id.tvDate);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);
        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);

        dbHelper = new ExpenseDbHelper(this);

        Calendar calendar = Calendar.getInstance();

        selectedDate = calendar.getTimeInMillis();

        updateDateText();

        tvDate.setOnClickListener(v -> {

            calendar.setTimeInMillis(selectedDate);


            DatePickerDialog dialog = new DatePickerDialog(this,

                    (view, year, month, dayOfMonth) -> {

                        Calendar selected = Calendar.getInstance();

                        selected.set(year, month, dayOfMonth);


                        selectedDate = selected.getTimeInMillis();


                        updateDateText();
                    },


                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


            dialog.show();

        });


        String[] categories = {"Food", "Transport", "Bills", "Entertainment", "Shopping", "Health", "Education", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);

        spinnerCategory.setAdapter(adapter);

        btnSaveExpense.setOnClickListener(v -> {

            saveExpense();

        });


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


        Expense expense = new Expense(title, amount, category, selectedDate);


        long id = dbHelper.insertExpense(expense);


        if (id != -1) {

            Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show();


            finish();

        } else {

            Toast.makeText(this, "Failed to save expense", Toast.LENGTH_SHORT).show();
        }

    }
}
