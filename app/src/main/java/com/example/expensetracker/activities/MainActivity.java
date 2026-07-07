package com.example.expensetracker.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.adapter.ExpenseAdapter;
import com.example.expensetracker.database.ExpenseDbHelper;
import com.example.expensetracker.model.Expense;

import java.util.ArrayList;

import com.example.expensetracker.R;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private ExpenseDbHelper dbHelper;
    private ArrayList<Expense> expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvExpenses);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new ExpenseDbHelper(this);

        expenseList = dbHelper.getAllExpenses();

        adapter = new ExpenseAdapter(expenseList);

        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}