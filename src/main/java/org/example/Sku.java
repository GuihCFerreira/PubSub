package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sku {

    @JsonProperty("id")
    private String id;

    @JsonProperty("value")
    private double value;

    public String getId() {
        return id;
    }

    public double getValue() {
        return value;
    }
}
