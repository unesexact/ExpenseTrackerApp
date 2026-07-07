package com.example.expensetracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.model.Expense;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private ArrayList<Expense> expenseList;


    public ExpenseAdapter(ArrayList<Expense> expenseList) {
        this.expenseList = expenseList;
    }


    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);

        return new ExpenseViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {

        Expense expense = expenseList.get(position);
        holder.tvExpenseTitle.setText(expense.getTitle());

        holder.tvExpenseAmount.setText(String.valueOf(expense.getAmount()));

        holder.tvExpenseCategory.setText(expense.getCategory());

        holder.tvExpenseDate.setText(String.valueOf(expense.getDate()));

    }


    @Override
    public int getItemCount() {
        return expenseList.size();
    }


    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        TextView tvExpenseTitle;
        TextView tvExpenseAmount;
        TextView tvExpenseCategory;
        TextView tvExpenseDate;


        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            tvExpenseTitle = itemView.findViewById(R.id.tvExpenseTitle);
            tvExpenseAmount = itemView.findViewById(R.id.tvExpenseAmount);
            tvExpenseCategory = itemView.findViewById(R.id.tvExpenseCategory);
            tvExpenseDate = itemView.findViewById(R.id.tvExpenseDate);
        }
    }
}