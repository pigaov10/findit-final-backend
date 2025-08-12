package com.imogo.imogo_backend.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    BUYER, SELLER, AGENT;

    @JsonCreator
    public static UserRole fromString(String value) {
        return UserRole.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toString() {
        return name().toLowerCase();
    }
}
