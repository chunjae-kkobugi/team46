package com.memomo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Team46Application {

    public static void main(String[] args) {
        SpringApplication.run(Team46Application.class, args);
    }

}
