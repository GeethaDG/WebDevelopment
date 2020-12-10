package com.example.Bachat;

public class GraphCategory {
    private String categoryName;
    private String iconURL;
    private float percentage;
    private float amount;


    public GraphCategory(String categoryName,String iconURL,float amount, float percentage) {
        this.categoryName = categoryName;
        this.iconURL = iconURL;
        this.percentage = percentage;
        this.amount=amount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIconURL() { return iconURL; }

    public void setIconURL(String iconURL) { this.iconURL = iconURL; }

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(String name) {
        this.percentage = percentage;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(String name) {
        this.amount = amount;
    }

}