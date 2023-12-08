package com.rushional.cities.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name = "country")
@Entity(name = "country")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Country extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
