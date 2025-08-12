package com.imogo.imogo_backend.dto;

import java.math.BigDecimal;

public class LatLngDTO {
    private final BigDecimal latitude;
    private final BigDecimal longitude;

    public LatLngDTO(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }
}
