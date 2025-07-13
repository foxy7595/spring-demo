package com.example.springdemo.repository;

import com.example.springdemo.model.auth.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by username
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if user exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Find user by username or email
     */
    @Query("{'$or': [{'username': ?0}, {'email': ?1}]}")
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    /**
     * Check if user exists by username or email
     */
    @Query("{'$or': [{'username': ?0}, {'email': ?1}]}")
    boolean existsByUsernameOrEmail(String username, String email);
} 