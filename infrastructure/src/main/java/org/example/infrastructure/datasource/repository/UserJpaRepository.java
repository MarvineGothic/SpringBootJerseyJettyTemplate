package org.example.infrastructure.datasource.repository;

import org.example.infrastructure.datasource.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses WHERE u.id = :id")
    Optional<User> findByIdWithAddresses(@Param("id") Long id);

    //    @Query("select u from user u where u.firstName = :firstName and u.lastName = :lastName")
//    List<User> findByLastNameAndFirstName(@Param("lastName") String lastName,
//                                   @Param("firstName") String firstName);
//    @Query("SELECT u FROM user u WHERE LOWER(u.firstName) = LOWER(:firstName)")
//    User retrieveByName(@Param("firstName") String firstName);
}
