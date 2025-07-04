package org.example.infrastructure.persistence.repository;

import org.example.infrastructure.persistence.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressJpaRepository extends JpaRepository<Address, Long> {
}
