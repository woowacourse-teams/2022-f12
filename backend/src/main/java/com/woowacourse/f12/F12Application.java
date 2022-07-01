package com.woowacourse.f12;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class F12Application {

    public static void main(String[] args) {
        SpringApplication.run(F12Application.class, args);
    }

}
