package com.imogo.imogo_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "property_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImobPropertyImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image_data", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    @JsonBackReference
    private ImobProperty imobProperty;
}
