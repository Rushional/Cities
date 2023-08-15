package com.rushional.cities.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;

@Table(name = "city")
@Entity(name = "city")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CityEntity extends AbstractAuditEntity {

    @Serial
    private static final long serialVersionUID = -4350213178619263160L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CountryEntity country;
}
