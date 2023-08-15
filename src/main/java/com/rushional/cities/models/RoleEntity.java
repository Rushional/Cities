package com.rushional.cities.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

@Table(name = "role")
@Entity(name = "role")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RoleEntity extends AbstractAuditEntity {

    @Serial
    private static final long serialVersionUID = -4350213178619263160L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private List<CustomerEntity> customers;
}
