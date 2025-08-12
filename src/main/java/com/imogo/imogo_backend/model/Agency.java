package com.imogo.imogo_backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "agency")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Agency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "license_number", length = 50)
    private String licenseNumber;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String phone;

    @Column(name = "tax_id", length = 20)
    private String taxId;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String address;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String district;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(length = 255)
    private String website;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @NotBlank
    @Column(length = 20)
    private String status; // "active" | "inactive"

    @OneToMany(mappedBy = "agency", cascade = CascadeType.ALL)
    private List<Agent> agents;
}
