package com.example.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;

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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private ExpenseDbHelper dbHelper;
    private ArrayList<Expense> expenseList;
    private FloatingActionButton fabAddExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvExpenses);
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

        fabAddExpense.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dbHelper != null) {

            expenseList.clear();
            expenseList.addAll(dbHelper.getAllExpenses());
            adapter.notifyDataSetChanged();

        }
    }

}