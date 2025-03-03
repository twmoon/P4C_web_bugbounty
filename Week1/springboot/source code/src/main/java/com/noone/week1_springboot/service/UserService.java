package com.noone.week1_springboot.service;

import com.noone.week1_springboot.entity.User;
import com.noone.week1_springboot.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> findByUsernameAndPasswordUnsafe(String username, String password) {
        // SQL 인젝션에 취약한 동적 쿼리 사용
        String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
        return entityManager.createNativeQuery(query, User.class).getResultList();
    }
}