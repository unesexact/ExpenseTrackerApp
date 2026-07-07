package com.example.expensetracker.model;

public class Expense {

    private int id;
    private String title;
    private double amount;
    private String category;
    private long date;

    public Expense(String title, double amount, String category, long date) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public Expense(int id, String title, double amount, String category, long date) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public long getDate() { return date; }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(long date) { this.date = date; }
}