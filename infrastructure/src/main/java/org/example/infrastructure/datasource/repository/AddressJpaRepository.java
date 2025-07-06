package org.example.infrastructure.datasource.repository;

import org.example.infrastructure.datasource.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressJpaRepository extends JpaRepository<AddressEntity, Long> {
}
