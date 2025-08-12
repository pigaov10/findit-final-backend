package com.imogo.imogo_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "property_location")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImobPropertyLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String zipcode;

    @OneToOne(mappedBy = "propertyLocation", fetch = FetchType.LAZY)
    private ImobProperty property;
}
