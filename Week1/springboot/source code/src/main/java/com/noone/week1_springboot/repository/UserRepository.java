package com.noone.week1_springboot.repository;

import com.noone.week1_springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 문자열 연결을 통한 동적 쿼리 생성
    @Query(value = "SELECT * FROM users WHERE username = ?1 AND password = ?2", nativeQuery = true)
    List<User> findByUsernameAndPasswordUnsafe(String username, String password);

    // 파라미터화된 쿼리 사용
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
    List<User> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}