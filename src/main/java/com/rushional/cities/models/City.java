package com.rushional.cities.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Table(name = "city")
@Entity(name = "city")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class City extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Country country;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "logo_path")
    private String logoPath;
}
