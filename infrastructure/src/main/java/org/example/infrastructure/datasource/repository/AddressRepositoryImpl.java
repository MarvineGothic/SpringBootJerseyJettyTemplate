package org.example.infrastructure.datasource.repository;

import lombok.RequiredArgsConstructor;
import org.example.domain.entity.AddressEntity;
import org.example.domain.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class AddressRepositoryImpl implements AddressRepository {
    private final AddressJpaRepository addressJpaRepository;

    @Override
    public AddressEntity save(AddressEntity address) {
        return AddressRepositoryMapper.toDomain(addressJpaRepository.save(AddressRepositoryMapper.fromDomain(address)));
    }

    @Override
    public Optional<AddressEntity> findById(Long id) {
        return addressJpaRepository.findById(id).map(AddressRepositoryMapper::toDomain);
    }
}
