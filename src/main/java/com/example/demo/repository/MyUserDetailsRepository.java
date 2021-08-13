package com.example.demo.repository;

import com.example.demo.model.MyUserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserDetailsRepository extends MongoRepository<MyUserDetails, String> {
    @Query("{'username' :  ?0}")
    Optional<MyUserDetails> findByUsername(String username);

    @Query("{'email' :  ?0}")
    Optional<MyUserDetails> findByEmail(String email);
}
