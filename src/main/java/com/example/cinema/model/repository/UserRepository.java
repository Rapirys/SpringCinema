package com.example.cinema.model.repository;

import com.example.cinema.config.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
    User findByUsernameOrEmail(String username, String email);
}