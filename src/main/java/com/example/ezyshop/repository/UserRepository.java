package com.example.ezyshop.repository;

import com.example.ezyshop.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    public Optional<User> findByUsername(String username);

    public Optional<User> findByEmail(String email);

    public boolean existsByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT u
    FROM User u
    WHERE u.username = :username
    """)
    public Optional<User> findByUsernameForUpdate(@Param("username") String username);
}
