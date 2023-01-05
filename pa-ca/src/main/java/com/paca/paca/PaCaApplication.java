package com.paca.paca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class PaCaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaCaApplication.class, args);
    }

    @GetMapping("/*")
    public String helloWorld(String s) { return ("Hello World");}
}
