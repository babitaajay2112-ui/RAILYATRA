package com.railway.trainproject.repository;

import com.railway.trainproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Email se user find karna
    Optional<User> findByEmail(String email);

    // Check karna ki email already registered hai ya nahi
    boolean existsByEmail(String email);
}