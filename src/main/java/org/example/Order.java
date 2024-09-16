package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Order {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("type")
    private String type;

    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("items")
    private List<OrderItem> items;

    public String getUuid() {
        return uuid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getType() {
        return type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "uuid='" + uuid + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", type='" + type + '\'' +
                ", customer=" + customer +
                ", items=" + items +
                '}';
    }
}
