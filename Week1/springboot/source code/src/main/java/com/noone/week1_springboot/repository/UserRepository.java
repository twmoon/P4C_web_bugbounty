package com.noone.week1_springboot.repository;

import com.noone.week1_springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // SQL 인젝션에 취약한 메서드: 문자열 연결을 통한 동적 쿼리 생성
    @Query(value = "SELECT * FROM users WHERE username = ?1 AND password = ?2", nativeQuery = true)
    List<User> findByUsernameAndPasswordUnsafe(String username, String password);
}