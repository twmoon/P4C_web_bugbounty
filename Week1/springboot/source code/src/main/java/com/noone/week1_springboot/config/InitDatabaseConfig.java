package com.noone.week1_springboot.config;

import com.noone.week1_springboot.entity.User;
import com.noone.week1_springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitDatabaseConfig implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // 기존 데이터 삭제 후 새 데이터 추가
        userRepository.deleteAll();

        // 데모용 사용자 추가
        userRepository.save(User.builder().username("admin").password("flag{sqli_is_fun}").build());
        userRepository.save(User.builder().username("user").password("user").build());
        userRepository.save(User.builder().username("guest").password("guest").build());
    }
}