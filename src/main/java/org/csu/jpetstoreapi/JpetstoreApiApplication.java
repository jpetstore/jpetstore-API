package org.csu.jpetstoreapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.csu.jpetstoreapi.persistence")
public class JpetstoreApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpetstoreApiApplication.class, args);
    }

}
