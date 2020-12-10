package com.example.Bachat;

public class ThresholdListItem {
        private String Category;
        private String Amount;


        public ThresholdListItem(String category, String amount) {
            Category = category;
            Amount = amount;

        }
        public String getCategory() {
            return Category;
        }

        public void setCategory(String category) {
            Category = category;
        }

        public String getAmount() {
            return Amount;
        }

        public void setAmount(String amount) {
            Amount = amount;
        }

}
