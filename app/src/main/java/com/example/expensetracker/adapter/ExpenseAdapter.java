package com.example.expensetracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.model.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private ArrayList<Expense> expenseList;
    private OnExpenseClickListener listener;

    public interface OnExpenseClickListener {

        void onExpenseClick(Expense expense);

    }

    public ExpenseAdapter(ArrayList<Expense> expenseList, OnExpenseClickListener listener) {

        this.expenseList = expenseList;
        this.listener = listener;

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
        holder.tvExpenseAmount.setText(String.format(Locale.getDefault(), "%.2f MAD", expense.getAmount()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        String date = sdf.format(new Date(expense.getDate()));

        holder.tvExpenseCategory.setText(expense.getCategory() + " - " + date);

        holder.tvExpenseDate.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> listener.onExpenseClick(expense));

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