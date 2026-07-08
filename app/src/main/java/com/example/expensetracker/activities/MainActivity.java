package com.example.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.ExpenseAdapter;
import com.example.expensetracker.database.ExpenseDbHelper;
import com.example.expensetracker.model.Expense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private ExpenseDbHelper dbHelper;
    private ArrayList<Expense> expenseList;
    private FloatingActionButton fabAddExpense;
    private TextView tvTotalAmount;
    private Spinner spinnerCategoryFilter;
    private Spinner spinnerMonthFilter;
    private TextView tvEmptyState;
    private String selectedCategory = "All";
    private int selectedMonth = -1;
    private int selectedYear = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvExpenses);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new ExpenseDbHelper(this);
        expenseList = dbHelper.getAllExpenses();

        adapter = new ExpenseAdapter(expenseList, expense -> {

            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            intent.putExtra("id", expense.getId());
            intent.putExtra("title", expense.getTitle());
            intent.putExtra("amount", expense.getAmount());
            intent.putExtra("category", expense.getCategory());
            intent.putExtra("date", expense.getDate());

            startActivity(intent);

        });

        recyclerView.setAdapter(adapter);

        fabAddExpense = findViewById(R.id.fabAddExpense);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        spinnerCategoryFilter = findViewById(R.id.spinnerCategoryFilter);
        spinnerMonthFilter = findViewById(R.id.spinnerMonthFilter);

        fabAddExpense.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });

        setupCategoryFilter();
        setupMonthFilter();
        selectedYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dbHelper != null) {

            expenseList.clear();
            expenseList.addAll(dbHelper.getAllExpenses());
            adapter.notifyDataSetChanged();
            updateTotalExpenses();
            updateEmptyState();

        }
    }

    private void updateTotalExpenses() {

        double total = dbHelper.getTotalExpenses();

        tvTotalAmount.setText(String.format(java.util.Locale.getDefault(), "%.2f MAD", total));
    }

    private void setupCategoryFilter() {

        String[] categories = {"All", "Food", "Transport", "Bills", "Entertainment", "Shopping", "Health", "Education", "Other"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);


        spinnerCategoryFilter.setAdapter(adapter);


        spinnerCategoryFilter.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {

                selectedCategory = categories[position];

                applyFilters();

            }


            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {

            }
        });
    }

    private void setupMonthFilter() {

        String[] months = {"All Months", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);


        spinnerMonthFilter.setAdapter(adapter);


        spinnerMonthFilter.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {

                selectedMonth = position - 1;

                applyFilters();

            }


            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {

            }
        });
    }

    private void applyFilters() {

        if (selectedMonth == -1) {

            expenseList.clear();

            if (selectedCategory.equals("All")) {

                expenseList.addAll(dbHelper.getAllExpenses());

            } else {

                expenseList.addAll(dbHelper.getFilteredExpenses(selectedCategory, 0, Long.MAX_VALUE));
            }

        } else {

            Calendar start = Calendar.getInstance();

            start.set(selectedYear, selectedMonth, 1, 0, 0, 0);


            Calendar end = Calendar.getInstance();

            end.set(selectedYear, selectedMonth, start.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);


            expenseList.clear();

            expenseList.addAll(dbHelper.getFilteredExpenses(selectedCategory, start.getTimeInMillis(), end.getTimeInMillis()));
        }


        adapter.notifyDataSetChanged();
        updateTotalExpenses();
        updateEmptyState();
    }

    private void updateEmptyState() {

        if (expenseList.isEmpty()) {

            tvEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        } else {

            tvEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        }
    }

}