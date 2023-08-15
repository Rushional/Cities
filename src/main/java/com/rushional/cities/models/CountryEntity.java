package com.rushional.cities.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Table(name = "country")
@Entity(name = "country")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CountryEntity extends AbstractAuditEntity {

    @Serial
    private static final long serialVersionUID = -4350213178619263160L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "flag_path")
    private String flagPath;

}
