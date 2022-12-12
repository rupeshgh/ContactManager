package com.example.demo.Repository;

import com.example.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "select u from User u where u.email=:name")
    User findUserByUsername (@Param("name")String name);
    @Query(value = "select u from User u where u.email=:email")
    Optional<User> findByEmail(@Param("email") String email);
}
