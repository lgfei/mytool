package com.lgfei.mytool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MytoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(MytoolApplication.class, args);
        System.out.println("http://localhost:8080/swagger-ui/index.html");
    }

}
