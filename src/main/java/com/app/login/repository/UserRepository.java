package com.app.login.repository;

import com.app.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Поиск пользователя по email
    @Query(value = "select * from clients where email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);

    // Поиск пользователя по username
    @Query(value = "select * from clients where userName = :userName", nativeQuery = true)
    Optional<User> findByUserName(String userName);

}
