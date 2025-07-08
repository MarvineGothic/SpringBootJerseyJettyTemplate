package org.example.infrastructure.datasource.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;

@MappedSuperclass
public abstract class JpaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.PRIVATE)
    private Long id;
}
