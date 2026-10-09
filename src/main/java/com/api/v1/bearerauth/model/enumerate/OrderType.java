package com.api.v1.bearerauth.model.enumerate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderType {
    DINE_IN("Dine In"), TAKEAWAY("Takeaway"), DELIVERY("Delivery");

    private final String label;
    OrderType(String label) { this.label = label; }

    @JsonValue
    public String getLabel() { return label; }

    @JsonCreator
    public static OrderType fromValue(String value) {
        if (value == null) return null;
        String v = value.trim().toUpperCase().replace(' ', '_');
        return valueOf(v);
    }
}
