package org.example.database.repository;

import org.example.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    //    @Query("select u from user u where u.firstName = :firstName and u.lastName = :lastName")
//    List<User> findByLastNameAndFirstName(@Param("lastName") String lastName,
//                                   @Param("firstName") String firstName);
//    @Query("SELECT u FROM user u WHERE LOWER(u.firstName) = LOWER(:firstName)")
//    User retrieveByName(@Param("firstName") String firstName);
}
