package com.example.expensetracker.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private TextView tvDate;
    private long selectedDate;
    private Button btnSaveExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_expense);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        tvDate = findViewById(R.id.tvDate);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);

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

            System.out.println("Save clicked");

        });


    }

    private void updateDateText() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        tvDate.setText(sdf.format(selectedDate));
    }
}
