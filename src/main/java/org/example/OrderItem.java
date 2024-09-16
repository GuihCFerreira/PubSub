package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItem {

    @JsonProperty("id")
    private int id;

    @JsonProperty("sku")
    private Sku sku;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("category")
    private Category category;

    public int getId() {
        return id;
    }

    public Sku getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public Category getCategory() {
        return category;
    }

    public static class Category {

        @JsonProperty("id")
        private String id;

        @JsonProperty("sub_category")
        private SubCategory subCategory;

        public String getId() {
            return id;
        }

        public SubCategory getSubCategory() {
            return subCategory;
        }
    }

    public static class SubCategory {

        @JsonProperty("id")
        private String id;

        public String getId() {
            return id;
        }
    }


}
