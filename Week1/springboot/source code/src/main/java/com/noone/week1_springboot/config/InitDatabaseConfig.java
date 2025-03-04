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
        userRepository.deleteAll();

        userRepository.save(User.builder().username("admin").password("flag{sqli_is_fun}").build());
        userRepository.save(User.builder().username("user").password("user").build());
        userRepository.save(User.builder().username("guest").password("guest").build());
    }
}